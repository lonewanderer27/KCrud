package com.jay_puzon.kcrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class KCrudMainActivity : AppCompatActivity() {
    private var FName: EditText? = null
    private var MName: EditText? = null
    private var LName: EditText? = null
    private var BtnAdd: Button? = null
    private var BtnView: Button? = null
    private var BtnDelete: Button? = null
    private var Conn: SQLiteDB? = SQLiteDB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kcrud_activity_main)

        FName = findViewById(R.id.fName)
        MName = findViewById(R.id.mName)
        LName = findViewById(R.id.lName)
        val nameFields = listOf(
            FName, MName, LName
        )

        BtnAdd = findViewById(R.id.btnAdd)
        BtnView = findViewById(R.id.btnView)
        BtnDelete = findViewById(R.id.btnClear)

        BtnView!!.setOnClickListener {
            Log.i("JobAlleyMainActivity", "BtnView clicked!")

            val callRecords = Intent(this, KCrudRecords::class.java)
            startActivity(callRecords)
        }

        BtnDelete!!.setOnClickListener {
            if (!Conn!!.NotEmpty()) {
                Log.i("DeleteRecords", "No records to delete")
                Toast.makeText(this, "Empty records", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Conn!!.DeleteRecords()) {
                Log.i("DeleteRecords", "All records have been deleted!")
                Toast.makeText(this, "Records have been cleared!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("DeleteRecords", "Error on clearing records!")
                Toast.makeText(
                    this,
                    "There has been an error on deleting records!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        BtnAdd!!.setOnClickListener {
            Log.i("JobAlleyMainActivity", "BtnAdd clicked!")

            // get the values from the edit text fields
            val names = listOf(
                FName!!.text.toString() + "",
                MName!!.text.toString() + "",
                LName!!.text.toString() + ""
            )

            // check if each field is filled up
            names.forEachIndexed { i, name ->
                if (name == "") {
                    nameFields[i]!!.error = "Please fill up this field!"
                    nameFields[i]!!.requestFocus();
                    return@setOnClickListener
                }
            }

            // check if there's existing record, if there is, don't add
            if (Conn!!.RecordExists(names[0], names[1], names[2])) {
                Log.i("AddRecord", "Record already exists");
                Toast.makeText(this, "Conflicts with existing record", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            // check if adding the record was successful
            if (Conn!!.AddRecord(names[0], names[1], names[2])) {
                Log.i("AddRecord", "Record saving successful!");
                Toast.makeText(this, "Record saved!", Toast.LENGTH_SHORT).show()

                // clear all edit text fields
                nameFields.forEach { editText ->
                    editText!!.setText("")
                }
            } else {
                Log.e("AddRecord", "Record saving unsuccessful!");
                Toast.makeText(this, "Record not saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}