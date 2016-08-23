package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangePwActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.et_changepw_oldpw)
    EditText etChangepwOldpw;
    @Bind(R.id.et_changepw_newpw)
    EditText etChangepwNewpw;
    @Bind(R.id.img_changepw_newpw_see)
    ImageView imgChangepwNewpwSee;
    @Bind(R.id.et_changepw_confirmpw)
    EditText etChangepwConfirmpw;
    @Bind(R.id.img_changepw_confirmpw_see)
    ImageView imgChangepwConfirmpwSee;
    @Bind(R.id.ib_changepw_confirm)
    ImageButton ibChangepwConfirm;

    private Boolean NEWPW_SEE = true;
    private Boolean CONFIRMPW_SEE= true;

    private static final String TAG = "ChangePwActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        initView();
        initEvent();
    }

    private void initEvent() {

        imgLogo.setOnClickListener(this);
        ibChangepwConfirm.setOnClickListener(this);
        imgChangepwNewpwSee.setOnClickListener(this);
        imgChangepwConfirmpwSee.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("修改密码");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_logo:
                ChangePwActivity.this.finish();
                break;

            case R.id.ib_changepw_confirm:

                if (!TextUtils.isEmpty(etChangepwOldpw.getText().toString())
                        &&!TextUtils.isEmpty(etChangepwNewpw.getText().toString())
                        &&!TextUtils.isEmpty(etChangepwConfirmpw.getText().toString())){
                    if (etChangepwNewpw.getText().toString().equals(etChangepwConfirmpw.getText().toString())){

                        if (etChangepwConfirmpw.length() >= 6){

                            changePw();
                        }else{
                            Toast.makeText(ChangePwActivity.this, "密码长度不够", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ChangePwActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    if (TextUtils.isEmpty(etChangepwOldpw.getText().toString())){
                        Toast.makeText(ChangePwActivity.this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(etChangepwNewpw.getText().toString())){
                        Toast.makeText(ChangePwActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(etChangepwConfirmpw.getText().toString())){
                        Toast.makeText(ChangePwActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.img_changepw_newpw_see:
                if (NEWPW_SEE){
                    etChangepwNewpw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etChangepwNewpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                NEWPW_SEE = !NEWPW_SEE;

                CharSequence charSequence = etChangepwNewpw.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }

                break;

            case R.id.img_changepw_confirmpw_see:

                if (CONFIRMPW_SEE){
                    etChangepwConfirmpw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etChangepwConfirmpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                CONFIRMPW_SEE = !CONFIRMPW_SEE;

                CharSequence charSequence1 = etChangepwConfirmpw.getText();
                if (charSequence1 instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence1;
                    Selection.setSelection(spanText, charSequence1.length());
                }

                break;

        }
    }

    private void changePw() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.CHANGE_PW, RequestMethod.POST);
        jsonObjectRequest.add("phone", SpUtil.getString(ChangePwActivity.this,"phone",""));
        jsonObjectRequest.add("passwd",etChangepwOldpw.getText().toString());
        jsonObjectRequest.add("passwds",etChangepwConfirmpw.getText().toString());
        CallServer.getInstance().add(ChangePwActivity.this,jsonObjectRequest,callback, UrlTag.CHANGE_PW,true,false,true);
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what==UrlTag.CHANGE_PW){
                LogUtil.d(TAG,response.toString());

                try {
                    JSONObject jsonObject = response.get();
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){

                        JSONObject data = jsonObject.getJSONObject("data");
                        String isok = data.getString("isok");
                        if (isok.equals("1")){
                            Toast.makeText(ChangePwActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            CustomApplication.removeAllAct();
                            Intent intent = new Intent(ChangePwActivity.this, SelectIdent.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(ChangePwActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ChangePwActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };
}
