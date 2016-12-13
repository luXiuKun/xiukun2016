package com.fangzhurapp.technicianport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectIdent extends Activity implements View.OnClickListener {

    @Bind(R.id.ib_selectident_boss)
    ImageButton ibSelectidentBoss;
    @Bind(R.id.ib_selectident_technician)
    ImageButton ibSelectidentTechnician;
    @Bind(R.id.ib_selectident_partner)
    ImageButton ibSelectidentPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ident);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        ibSelectidentBoss.setOnClickListener(this);
        ibSelectidentTechnician.setOnClickListener(this);
        ibSelectidentPartner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ib_selectident_boss:
                SpUtil.putString(SelectIdent.this, "selectident", "2");
                Intent boss = new Intent(SelectIdent.this, LoginActivity.class);
                startActivity(boss);
                SelectIdent.this.finish();
                break;

            case R.id.ib_selectident_technician:
                SpUtil.putString(SelectIdent.this, "selectident", "1");
                Intent technician = new Intent(SelectIdent.this, LoginActivity.class);
                startActivity(technician);
                SelectIdent.this.finish();
                break;

            case R.id.ib_selectident_partner:
                SpUtil.putString(SelectIdent.this, "selectident", "3");
                Intent partner = new Intent(SelectIdent.this, LoginActivity.class);
                startActivity(partner);
                SelectIdent.this.finish();
                break;
        }
    }
}
