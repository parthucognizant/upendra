package com.example.rechargeapp;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rechargeapp.parser.JSONParser;

public class RegisterNumberActivity extends ActionBarActivity {

	private ProgressDialog pDialog;
	Spinner spinner_type,spinner_provider;
	EditText edit_mobilenumber;
	String myJSON;
	private static final String MyPREFERENCES = "LoginSession";
	SharedPreferences sharedpreferences;
	String userid;

	private static String url = "http://10.251.163.5:8088/MFRPServices/recharge_save_number";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_number);
		edit_mobilenumber = (EditText)findViewById(R.id.edit_mobilenumber);
		spinner_type = (Spinner)findViewById(R.id.spinner_type);
		spinner_provider = (Spinner)findViewById(R.id.spinner_provider);
		sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		userid = sharedpreferences.getString("userKey", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void registerclick(View v){
		String mobile_number = edit_mobilenumber.getText().toString();
		if(mobile_number.length()!=10){
			edit_mobilenumber.setError("Check mobile number");
			edit_mobilenumber.requestFocus();
		}
		else
		{
			new AttemptRegister().execute();
		}
	}

	class AttemptRegister extends AsyncTask<String, String, Boolean> {

		JSONParser jsonParser = new JSONParser();
		private static final String TAG = "Register";


		@Override protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterNumberActivity.this);
			pDialog.setMessage("Hold on Registering...");
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
				jsonObject.accumulate("user_id",userid);
				jsonObject.accumulate("number",edit_mobilenumber.getText().toString());
				jsonObject.accumulate("type",spinner_type.getSelectedItem().toString());
				jsonObject.accumulate("provider",spinner_provider.getSelectedItem().toString());


				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json1 = jsonParser.makeHttpRequest(url,
						"POST", json);

				// check log cat fro response
				Log.d("Create Response", json1.toString());

				Log.i(TAG, ""+json1.toString());
				String success = json1.getString("status");
				if(success.equals("success"))
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
				Toast.makeText(RegisterNumberActivity.this, "Mobile Number Registered", Toast.LENGTH_SHORT).show();
				Intent homeintetnt = new Intent(RegisterNumberActivity.this, HomeActivity.class);
				startActivity(homeintetnt);
			}
			else
			{
				Toast.makeText(RegisterNumberActivity.this, "Please check the deails", Toast.LENGTH_SHORT).show();
			}

		}
	}
}
