package com.example.rechargeapp;


import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rechargeapp.parser.JSONParser;

public class TransactionActivity extends ActionBarActivity implements OnClickListener {

	EditText transaction_password;
	Button pay;
	private static String url = "http://10.251.163.5:8088/MFRPServices/validate_password";
	private ProgressDialog pDialog;
	String myJSON;
	String response = null;
	String cardnumber;
	String amount,mobilenumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		transaction_password = (EditText)findViewById(R.id.edit_transaction);
		pay = (Button)findViewById(R.id.button_pay);
		pay.setOnClickListener(this);
		cardnumber = this.getIntent().getExtras().getString("cardnumber");
		amount = this.getIntent().getExtras().getString("amount");
		mobilenumber = this.getIntent().getExtras().getString("mobilenumber");
	}

	public void onClick(View view) {
		if(transaction_password.getText().toString().equalsIgnoreCase("")){
			transaction_password.setError("Transaction password is empty");
		}
		else{
			new AttemptValidatePassword().execute();
		}
	}

	class AttemptValidatePassword extends AsyncTask<String, String, Boolean> {

		JSONParser jsonParser = new JSONParser();
		private static final String TAG = "Register";


		@Override protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TransactionActivity.this);
			pDialog.setMessage("validating Details...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try{
				/*String username = etusername.getText().toString();
				String password=etpassword.getText().toString();*/

				String json = "";

				// 3. build jsonObject
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("card_no",cardnumber);
				jsonObject.accumulate("transaction_password",transaction_password.getText().toString());
				jsonObject.accumulate("amount",amount);

				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json1 = jsonParser.makeHttpRequest(url,
						"POST", json);

				// check log cat fro response
				Log.d("Create Response", json1.toString());

				Log.i(TAG, ""+json1.toString());
				response = json1.getString("message");
				if(response.equals("Payment Success"))
					return true;


			}
			catch(Exception e)
			{

			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pDialog.hide();
			if(result){
				Toast.makeText(TransactionActivity.this, "Payment Done", Toast.LENGTH_SHORT).show();
				Intent successintetnt = new Intent(TransactionActivity.this, SuccessActivity.class);
				successintetnt.putExtra("mobilenumber", mobilenumber);
				startActivity(successintetnt);
			}
			else
			{
				Toast.makeText(TransactionActivity.this, response, Toast.LENGTH_SHORT).show();
			}

		}
	}

}