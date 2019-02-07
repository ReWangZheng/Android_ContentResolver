package com.example.wz.getcontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<String[]> list=new ArrayList<>();
    public adapter adp;
    Uri uri=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ListView textView=(ListView) findViewById(R.id.listView);
        adp=new adapter(this,R.id.listView,list);
        textView.setAdapter(adp);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nub=((TextView)view.findViewById(R.id.nubView)).getText().toString();
                uri=Uri.parse("tel:"+nub);

                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    call(uri);
                }else
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},2);

                }


            }
        });



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED)
        {
            ReadContact();

        }else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    ReadContact();
                }else {
                    Toast.makeText(this,"抱歉，没有权限执行",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    call(uri);

                }else
                {
                    Toast.makeText(this,"抱歉，没有权限执行",Toast.LENGTH_SHORT).show();
                }

        }



         }

    public void call(Uri uri)
    {
        Intent intent=new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        try{
            startActivity(intent);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    public void ReadContact()
    {
        ContentResolver contentResolver=getContentResolver();
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor=null;
        try{
            cursor=contentResolver.query(uri,null,null,null,null);
            if (cursor!=null)
            {
                while(cursor.moveToNext())
                {
                    String username=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phenub=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    list.add(new String[]{username,phenub});
                }
            }

        adp.notifyDataSetChanged();

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();

            }
        }

    }

    public class adapter extends ArrayAdapter<String>
    {
        private int mID;
        private List<String[]> list;
        public adapter(Context context, int resource,List<String[]> list) {
            super(context, resource);
            mID=resource;
            this.list=list;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.item,parent,false);
            TextView nameView=view.findViewById(R.id.name);
            TextView nubView=view.findViewById(R.id.nubView);
            String name=list.get(position)[0];
            String nub=list.get(position)[1];
            nameView.setText(name);
            nubView.setText(nub);
            return view;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }




}
