package com.example.rechargeapp;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.rechargeapp.parser.JSONParser;

public class RechargePaymentMainActivity extends ActionBarActivity implements OnFocusChangeListener, OnCheckedChangeListener {

	private int year, month, day;

	EditText expirydate;
	RadioGroup cardgroup;
	String cardtype;
	EditText cardnumber,cvv;
	private ProgressDialog pDialog;
	String myJSON;
	String response = null;
	String amount,mobilenumber;
	private static String url = "http://10.251.163.5:8088/MFRPServices/recharge_payment";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge_payment_main);

		expirydate = (EditText)findViewById(R.id.edit_expirydate);
		expirydate.setOnFocusChangeListener(this);

		cardgroup = (RadioGroup)findViewById(R.id.radioGroup_paymenttype);
		//cardtype = ((RadioButton)findViewById(cardgroup.getCheckedRadioButtonId())).getText().toString();
		cardgroup.setOnCheckedChangeListener(this);

		cardnumber = (EditText)findViewById(R.id.edit_cardnumber);
		cvv = (EditText)findViewById(R.id.edit_cvv);
		
		amount = this.getIntent().getExtras().getString("amount");
		mobilenumber = this.getIntent().getExtras().getString("mobilenumber");


	}

	@SuppressWarnings("deprecation")
	public void setDate() {
		showDialog(999);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker arg0, int year, int month, int day) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(year, month+1, day);
		}
	};

	private void showDate(int year, int month, int day) {
		expirydate.setText(new StringBuilder().append(year).append("-")
				.append(month).append("-").append(day));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recharge_payment_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onFocusChange(View v, boolean hasfocus) {
		if(hasfocus){
			setDate();
		}

	}
	public void proceedpay(View v){
		//Toast.makeText(RechargePaymentMainActivity.this, cardtype, Toast.LENGTH_SHORT).show();
		if (cardgroup.getCheckedRadioButtonId() == -1)
		{
			Toast.makeText(RechargePaymentMainActivity.this, "Please select any of the card type", Toast.LENGTH_SHORT).show();
		}
		else if(cardnumber.getText().toString().length() != 16){
			cardnumber.setError("Enter 16 digit Number");
		}
		else if(expirydate.getText().toString().equalsIgnoreCase("")){
			expirydate.setError("Enter date");
		}
		else if(cvv.getText().toString().length() != 3){
			cvv.setError("Enter 3 digit Number");
		}
		else{
			new AttemptPayment().execute();
		}

	}

	public void onCheckedChanged(RadioGroup group, int checkid) {

		switch (checkid) {
		case R.id.radio_credit:
			cardtype = "credit";
			break;
		case R.id.radio_debit:
			cardtype = "debit";
			break;
		default:
			break;
		}

	}
	class AttemptPayment extends AsyncTask<String, String, Boolean> {

		JSONParser jsonParser = new JSONParser();
		private static final String TAG = "Register";


		@Override protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RechargePaymentMainActivity.this);
			pDialog.setMessage("Checking Details...");
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
				jsonObject.accumulate("card_no",cardnumber.getText().toString());
				jsonObject.accumulate("exp_date",expirydate.getText().toString());
				jsonObject.accumulate("cvv",cvv.getText().toString());
				jsonObject.accumulate("amount",amount);
				jsonObject.accumulate("card_type",cardtype);


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
				if(response.equals("Validation Success"))
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
				Toast.makeText(RechargePaymentMainActivity.this, "You Are Almost Done", Toast.LENGTH_SHORT).show();
				Intent transactionintetnt = new Intent(RechargePaymentMainActivity.this, TransactionActivity.class);
				transactionintetnt.putExtra("cardnumber", cardnumber.getText().toString());
				transactionintetnt.putExtra("amount", amount);
				transactionintetnt.putExtra("mobilenumber", mobilenumber);
				startActivity(transactionintetnt);
			}
			else
			{
				Toast.makeText(RechargePaymentMainActivity.this, response, Toast.LENGTH_SHORT).show();
			}

		}
	}

}