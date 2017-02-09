package com.example.www.logregapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button btn_Reg;
    EditText Name,Email,Username,Password,Conpassword;
    String name,email,username,password,conpass;
    AlertDialog.Builder builder;
    String reg_url="http://192.168.212.2/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_Reg=(Button)findViewById(R.id.btnRegister);

        Name=(EditText)findViewById(R.id.edtName);
        Email=(EditText)findViewById(R.id.edtEmail);
        Username=(EditText)findViewById(R.id.edtUsernameS);
        Password=(EditText)findViewById(R.id.edtPasswordS);
        Conpassword=(EditText)findViewById(R.id.edtConPasswordS);
        builder=new AlertDialog.Builder(Register.this);

        btn_Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=Name.getText().toString().trim();
                email=Email.getText().toString().trim();
                username=Username.getText().toString().trim();
                password=Password.getText().toString().trim();
                conpass=Conpassword.getText().toString().trim();

                if(name.equals("")||email.equals("")||username.equals("")||password.equals("")||password.equals("")||conpass.equals("")){
                    builder.setTitle("Something Went Wrong !");
                    builder.setMessage("Please Fill all the Fields....");
                    displayAlert("input_error");
                }
                else{
                    if(!password.equals(conpass)){
                        builder.setTitle("Something Went Wrong !");
                        builder.setMessage("Your Passwords are not matching....");
                        displayAlert("input_error");
                    }
                    else{
                        StringRequest request=new StringRequest(Request.Method.POST, reg_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray=new JSONArray(response);
                                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                                            String code=jsonObject.getString("code");
                                            String message=jsonObject.getString("message");
                                            builder.setTitle("Server Response");
                                            builder.setMessage(message);
                                            displayAlert(code);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> parms=new HashMap<String, String>();
                                parms.put("name",name);
                                parms.put("email",email);
                                parms.put("user_name",username);
                                parms.put("password",password);
                                return parms;
                            }
                        };
                        MySingleton.getInstance(Register.this).addToRequestQueue(request);
                    }
                }
            }
        });
    }
    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface,int which){
                if(code.equals("")){
                    Password.setText("");
                    Conpassword.setText("");
                }
                else if (code.equals("Registration_Success")){
                        finish();
                }
                else if (code.equals("Registration_Failed")){
                        Name.setText("");
                        Email.setText("");
                        Username.setText("");
                        Password.setText("");
                        Conpassword.setText("");
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
