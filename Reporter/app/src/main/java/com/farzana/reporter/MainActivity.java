package com.farzana.reporter;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    TabHost tabHost;
    static final String url = "http://172.20.42.82/project/";
    //static final String url = "http://192.168.0.104/project/";
    static final String url_report = url+"get_report.php";
    static final String url_submit = url+"submit.php";
    static final String url_report_rreg = url+"g_register.php";
    static final String url_report_feed = url+"g_feedback.php";
    static final String url_report_comment = url+"g_comment.php";

    String global_username = "";
    Boolean isLoggedin = false;
    Boolean isAuthority = false;
    String global_authority = "";
    String submit_username = "";

    int explore_reportid;

    Spinner sp_authority;
    Spinner sp_department;
    Spinner submit_authority;
    Spinner submit_department;

    Spinner spinner_search_dept;
    Spinner spinner_search_auth;

    TableLayout table_explore;
    TableLayout tablelayout_user;

    TabHost.TabSpec tab2;


    int jreport_id[] = new int[100];
    String juser_id[] = new String[100];
    String jresponsible[] = new String[100];
    String jsubmission[] = new String[100];
    String jdescription[] = new String[100];

    int jlength;

    String list[] = {"FAQs", "Register", "Log In"};
    String userlist[] = {"My Reports", "My Profile", "Update Profile"};
    String loginlist[] = {"FAQs",""+global_username+"", "Log Out"};

    int runique_id[] = new int[100];
    String ruser_id[] = new String[100];
    String rfull_name[] = new String[100];
    String rcontact_no[] = new String[100];
    String rpassword[] = new String[100];
    String ruser_type[] = new String[100];
    String rresponsible[] = new String[100];
    String rsubmission[] = new String[100];
    String rupdated_at[] = new String[100];

    int rlength;

    int freport_id[] = new int[100];
    String fresponsible[] = new String[100];
    String fsubmission[] = new String[100];
    String ffeedback[] = new String[100];

    int flength;


    int creport_id[] = new int[100];
    String cuser_id[] = new String[100];
    String csubmission[] = new String[100];
    String ccomments[] = new String[100];

    int clength;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------TabHOst----------------------------------------------------------

        loadReports();
        submit_report();
        register();
        login();
        g_register();
        g_feedback();
        g_comment();
        submit_explore_comments();
        //search();



        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Home");
        tab1.setContent(R.id.Home);
        tab1.setIndicator("Home");
        tabHost.addTab(tab1);

        tab2 = tabHost.newTabSpec("Submit Report");
        tab2.setContent(R.id.submit);
        tab2.setIndicator("Submit Report");
        tabHost.addTab(tab2);


        TabHost.TabSpec tab3 = tabHost.newTabSpec("Xplore Reports");
        tab3.setContent(R.id.explore);
        tab3.setIndicator("Xplore Reports");
        tabHost.addTab(tab3);

        TabHost.TabSpec tab4 = tabHost.newTabSpec("Other");
        tab4.setContent(R.id.tab4);
        tab4.setIndicator("|||");
        //☰
        tabHost.addTab(tab4);


        //-----------------------------------------click on tabhost--------------------------------------------


        tabHost.getTabWidget().getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab4layoutsgone();

                if(isLoggedin == false)
                {
                    LinearLayout layoutlistview = (LinearLayout) findViewById(R.id.layout_listview);
                    layoutlistview.setVisibility(View.VISIBLE);
                }

                else if(isLoggedin == true)
                {

                    LinearLayout layoutlistview = (LinearLayout) findViewById(R.id.layout_listview_login);
                    layoutlistview.setVisibility(View.VISIBLE);
                }
                tabHost.setCurrentTab(3);

            }
        });


        tabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadReports();

                TableRow row = (TableRow)findViewById(R.id.tablerow_main);

                table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                table_explore.removeAllViews();

                LinearLayout layout_explore = (LinearLayout)findViewById(R.id.layout_explore);
                layout_explore.setVisibility(View.VISIBLE);

                LinearLayout layout_detail = (LinearLayout)findViewById(R.id.layout_detail);
                layout_detail.setVisibility(View.GONE);

                LinearLayout layout_search = (LinearLayout)findViewById(R.id.layout_search);
                layout_search.setVisibility(View.VISIBLE);

                table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                table_explore.addView(row);

                spinner_search_auth.setSelection(0);
                spinner_search_dept.setSelection(0);

                for(int i=0; i <jlength; i++)
                    addrows(i);

                tabHost.setCurrentTab(2);

            }
        });

        //-------------------------------------Spinner-----------------------------------------------------------


        sp_authority = (Spinner)findViewById(R.id.spinner);
        sp_department = (Spinner)findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.authority,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_authority.setAdapter(arrayAdapter);


        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this, R.array.department,android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_department.setAdapter(arrayAdapter2);


        spinner_search_auth = (Spinner)findViewById(R.id.spinner_search);
        spinner_search_dept = (Spinner)findViewById(R.id.spinner_search2);

        ArrayAdapter<CharSequence> arrayAdapter_s = ArrayAdapter.createFromResource(this, R.array.sauthority,android.R.layout.simple_spinner_item);
        arrayAdapter_s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_search_auth.setAdapter(arrayAdapter_s);


        ArrayAdapter<CharSequence> arrayAdapter2_s = ArrayAdapter.createFromResource(this, R.array.sdepartment,android.R.layout.simple_spinner_item);
        arrayAdapter2_s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_search_dept.setAdapter(arrayAdapter2_s);

        search();



        //---------------------------------------LIST VIEW !!!!!!!!!--------------------------------------------------------------

        listview();  //main listview






        ListAdapter adapter_user = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userlist);

        ListView listViewuser = (ListView) findViewById(R.id.listview_user);
        listViewuser.setAdapter(adapter_user);

        listViewuser.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {

                        if(j == 0) //MY REPORTS
                        {
                            tab4layoutsgone();

                            loadReports();

                            TableRow row = (TableRow)findViewById(R.id.tr_user_reports);

                            tablelayout_user = (TableLayout) findViewById(R.id.tablelayout_user);
                            tablelayout_user.removeAllViews();

                            ScrollView scrolluserreport = (ScrollView)findViewById(R.id.scroll_user_report);
                            scrolluserreport.setVisibility(View.VISIBLE);


                            tablelayout_user = (TableLayout) findViewById(R.id.tablelayout_user);
                            tablelayout_user.addView(row);

                            listview_users();

                         }

                        else if(j == 1) //MY PROFILE
                        {
                            tab4layoutsgone();

                            ScrollView scrolluserprofile = (ScrollView)findViewById(R.id.scroll_user_profile);
                            scrolluserprofile.setVisibility(View.VISIBLE);

                            //((TextView)tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText("hello");

                        }

                        else if(j == 2) //UPDATE PROFILE
                        {
                            tab4layoutsgone();


                            ScrollView scrollupdate = (ScrollView)findViewById(R.id.scroll_update);
                            scrollupdate.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );






    }

    public void addrows(int i)
    {
        //for(int i=0; i <jlength; i++)
        {

            TableRow tr = new TableRow(getApplicationContext());
            tr.setMinimumHeight(80);
            tr.setBackgroundResource(R.drawable.cell_shape_gray);
            tr.setClickable(true);
            tr.setOnClickListener(tablerowOnClickListener);


            TextView tv0 = new TextView(getApplicationContext());
            TextView tv1 = new TextView(getApplicationContext());
            TextView tv2 = new TextView(getApplicationContext());
            TextView tv3 = new TextView(getApplicationContext());

            String str = Integer.toString(jreport_id[i]);
            tv0.setText(str);
            tv1.setText(jsubmission[i]);
            tv2.setText(jdescription[i]);
            tv3.setText(jresponsible[i]);

            tv0.setTextColor(Color.BLACK);
            tv1.setTextColor(Color.BLACK);
            tv2.setTextColor(Color.BLACK);
            tv3.setTextColor(Color.BLACK);

            tv0.setGravity(Gravity.CENTER);
            tv1.setGravity(Gravity.CENTER);
            tv2.setGravity(Gravity.CENTER);
            tv3.setGravity(Gravity.CENTER);

            tv0.setPadding(10, 10, 10, 10);
            tv1.setPadding(10, 10, 10, 10);
            tv2.setPadding(10, 10, 10, 10);
            tv3.setPadding(10, 10, 10, 10);

            tv0.setBackgroundResource(R.drawable.cell_shape_gray);
            tv1.setBackgroundResource(R.drawable.cell_shape_gray);
            tv2.setBackgroundResource(R.drawable.cell_shape_gray);
            tv3.setBackgroundResource(R.drawable.cell_shape_gray);

            tv0.setMinimumHeight(80);
            tv1.setMinimumHeight(80);
            tv2.setMinimumHeight(80);
            tv3.setMinimumHeight(80);

            tr.addView(tv0);
            tr.addView(tv1);
            tr.addView(tv2);
            tr.addView(tv3);

            table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
            table_explore.addView(tr);
        }

    }

    public void search()
    {


        Button search_button = (Button) findViewById(R.id.button_search);
        search_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String str_auth = spinner_search_auth.getSelectedItem().toString();
                String str_dept = spinner_search_dept.getSelectedItem().toString();

                //String sub_authority = str_auth + ", " + str_dept + ", " + "BUET";

                loadReports();
                // if search by authority only
                if(!str_auth.equalsIgnoreCase("All") && str_dept.equalsIgnoreCase("All"))
                {
                    TableRow row = (TableRow)findViewById(R.id.tablerow_main);

                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.removeAllViews();


                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.addView(row);
                    for(int i=0; i<jlength; i++)
                    {
                        String authority = jresponsible[i];
                        String subString="";

                        int index = authority.indexOf(",");

                        if (index != -1)
                            subString= authority.substring(0, index);

                        Log.v("search", subString);




                        if(str_auth.equalsIgnoreCase(subString))
                        {
                            addrows(i);
                        }

                    }
                }


                // if search by department only
                else if(str_auth.equalsIgnoreCase("All") && !str_dept.equalsIgnoreCase("All"))
                {
                    TableRow row = (TableRow)findViewById(R.id.tablerow_main);

                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.removeAllViews();


                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.addView(row);
                    for(int i=0; i<jlength; i++)
                    {
                        String authority = jresponsible[i];
                        String subString="";

                        int index = authority.indexOf(",");

                        if (index != -1)
                            subString= authority.substring(index+2, authority.length()-1);

                        int index2 = subString.indexOf(",");

                        if (index2 != -1)
                            subString = subString.substring(0, index2);

                        Log.v("search", subString);




                        if(str_dept.equalsIgnoreCase(subString))
                        {
                            addrows(i);
                        }

                    }

                }

                // if search by both
                else if(!str_auth.equalsIgnoreCase("All") && !str_dept.equalsIgnoreCase("All"))
                {

                    TableRow row = (TableRow)findViewById(R.id.tablerow_main);

                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.removeAllViews();


                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.addView(row);
                    for(int i=0; i<jlength; i++)
                    {

                        String authority1 = jresponsible[i];
                        String subString1="";

                        int index1 = authority1.indexOf(",");

                        if (index1 != -1)
                            subString1= authority1.substring(0, index1);


                        String authority = jresponsible[i];
                        String subString="";

                        int index = authority.indexOf(",");

                        if (index != -1)
                            subString= authority.substring(index+2, authority.length()-1);

                        int index2 = subString.indexOf(",");

                        if (index2 != -1)
                            subString = subString.substring(0, index2);

                        Log.v("search", subString);




                        if(str_auth.equalsIgnoreCase(subString1)  && str_dept.equalsIgnoreCase(subString))
                        {
                            addrows(i);
                        }

                    }

                }

                else if(str_auth.equalsIgnoreCase("All") && str_dept.equalsIgnoreCase("All"))
                {

                    TableRow row = (TableRow)findViewById(R.id.tablerow_main);

                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.removeAllViews();


                    table_explore = (TableLayout) findViewById(R.id.tablelayout_explore);
                    table_explore.addView(row);
                    for(int i=0; i<jlength; i++)
                    {

                            addrows(i);

                    }

                }



            }
        });
    }


    public void listview()
    {
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(i == 0) //faq layout
                        {
                            tab4layoutsgone();

                            LinearLayout layoutfaq = (LinearLayout)findViewById(R.id.layout_faq);
                            layoutfaq.setVisibility(View.VISIBLE);
                        }

                        else if(i == 1) //sign up layout
                        {
                            tab4layoutsgone();

                            ScrollView scrollsignup = (ScrollView)findViewById(R.id.scroll_signup);
                            scrollsignup.setVisibility(View.VISIBLE);

                        }

                        else if(i == 2)
                        {
                            tab4layoutsgone();

                            ScrollView layoutlogin = (ScrollView)findViewById(R.id.layout_login);
                            layoutlogin.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

    }

    public void listview_login()
    {
        loginlist[1] = global_username;
        ListAdapter adapter_login = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, loginlist);

        ListView listViewlogin = (ListView) findViewById(R.id.listview_login);
        listViewlogin.setAdapter(adapter_login);

        listViewlogin.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(i == 0) //faq layout
                        {
                            tab4layoutsgone();

                            LinearLayout layoutfaq = (LinearLayout)findViewById(R.id.layout_faq);
                            layoutfaq.setVisibility(View.VISIBLE);
                        }

                        else if(i == 1) //sign up layout
                        {
                            tab4layoutsgone();

                            LinearLayout layout_listview_user = (LinearLayout)findViewById(R.id.layout_listview_user);
                            layout_listview_user.setVisibility(View.VISIBLE);

                            //((TextView)tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText("hello");

                        }

                        else if(i == 2) //
                        {
                            tab4layoutsgone();

                            LinearLayout layoutlistview = (LinearLayout)findViewById(R.id.layout_listview);
                            layoutlistview.setVisibility(View.VISIBLE);

                            global_username = "";
                            isLoggedin = false;
                            isAuthority = false;
                            global_authority = "";
                        }
                    }
                }
        );


    }

    public void listview_users()
    {
        if(isAuthority == false)
            for(int i=0; i <jlength; i++)
            {
                if(global_username.equals(juser_id[i]) && !global_username.equals(""))
                {

                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setMinimumHeight(80);
                    //tr.setBackgroundResource(R.drawable.cell_shape_gray);
                    tr.setClickable(true);
                    tr.setOnClickListener(tablerowOnClickListener_user);


                    TextView tv0 = new TextView(getApplicationContext());
                    TextView tv1 = new TextView(getApplicationContext());
                    TextView tv2 = new TextView(getApplicationContext());
                    TextView tv3 = new TextView(getApplicationContext());

                    String str = Integer.toString(jreport_id[i]);
                    tv0.setText(str);
                    tv1.setText(jsubmission[i]);
                    tv2.setText(jdescription[i]);
                    tv3.setText(jresponsible[i]);

                    tv0.setTextColor(Color.BLACK);
                    tv1.setTextColor(Color.BLACK);
                    tv2.setTextColor(Color.BLACK);
                    tv3.setTextColor(Color.BLACK);

                    tv0.setGravity(Gravity.CENTER);
                    tv1.setGravity(Gravity.CENTER);
                    tv2.setGravity(Gravity.CENTER);
                    tv3.setGravity(Gravity.CENTER);

                    tv0.setPadding(10, 10, 10, 10);
                    tv1.setPadding(10, 10, 10, 10);
                    tv2.setPadding(10, 10, 10, 10);
                    tv3.setPadding(10, 10, 10, 10);

                    tv0.setBackgroundResource(R.drawable.cell_shape_gray);
                    tv1.setBackgroundResource(R.drawable.cell_shape_gray);
                    tv2.setBackgroundResource(R.drawable.cell_shape_gray);
                    tv3.setBackgroundResource(R.drawable.cell_shape_gray);

                    tv0.setMinimumHeight(80);
                    tv1.setMinimumHeight(80);
                    tv2.setMinimumHeight(80);
                    tv3.setMinimumHeight(80);

                    tr.addView(tv0);
                    tr.addView(tv1);
                    tr.addView(tv2);
                    tr.addView(tv3);

                    tablelayout_user = (TableLayout) findViewById(R.id.tablelayout_user);
                    tablelayout_user.addView(tr);
                }
            }

        else if(isAuthority)

            for(int i=0; i <jlength; i++)
            {
                Log.v("hell", jresponsible[i]);
                if(global_authority.equals(jresponsible[i]))
                {

                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setMinimumHeight(80);
                    tr.setBackgroundResource(R.drawable.cell_shape_gray);
                    tr.setClickable(true);
                    tr.setOnClickListener(tablerowOnClickListener_authority);


                    TextView tv0 = new TextView(getApplicationContext());
                    TextView tv1 = new TextView(getApplicationContext());
                    TextView tv2 = new TextView(getApplicationContext());
                    TextView tv3 = new TextView(getApplicationContext());

                    String str = Integer.toString(jreport_id[i]);
                    tv0.setText(str);
                    tv1.setText(jsubmission[i]);
                    tv2.setText(jdescription[i]);
                    tv3.setText(jresponsible[i]);

                    tv0.setTextColor(Color.BLACK);
                    tv1.setTextColor(Color.BLACK);
                    tv2.setTextColor(Color.BLACK);
                    tv3.setTextColor(Color.BLACK);

                    tv0.setGravity(Gravity.CENTER);
                    tv1.setGravity(Gravity.CENTER);
                    tv2.setGravity(Gravity.CENTER);
                    tv3.setGravity(Gravity.CENTER);

                    tv0.setPadding(10, 10, 10, 10);
                    tv1.setPadding(10, 10, 10, 10);
                    tv2.setPadding(10, 10, 10, 10);
                    tv3.setPadding(10, 10, 10, 10);

                    tv0.setBackgroundResource(R.drawable.cell_shape_gray);
                    tv1.setBackgroundResource(R.drawable.cell_shape_gray);
                    tv2.setBackgroundResource(R.drawable.cell_shape_gray);
                    tv3.setBackgroundResource(R.drawable.cell_shape_gray);

                    tv0.setMinimumHeight(80);
                    tv1.setMinimumHeight(80);
                    tv2.setMinimumHeight(80);
                    tv3.setMinimumHeight(80);

                    tr.addView(tv0);
                    tr.addView(tv1);
                    tr.addView(tv2);
                    tr.addView(tv3);

                    tablelayout_user = (TableLayout) findViewById(R.id.tablelayout_user);
                    tablelayout_user.addView(tr);
                }
            }
    }


    public void tab4layoutsgone()
    {
        LinearLayout layoutlistview = (LinearLayout)findViewById(R.id.layout_listview);
        layoutlistview.setVisibility(View.GONE);

        LinearLayout layoutlistview_login = (LinearLayout)findViewById(R.id.layout_listview_login);
        layoutlistview_login.setVisibility(View.GONE);

        LinearLayout layoutlistview_user = (LinearLayout)findViewById(R.id.layout_listview_user);
        layoutlistview_user.setVisibility(View.GONE);

        ScrollView scrollupdate = (ScrollView)findViewById(R.id.scroll_update);
        scrollupdate.setVisibility(View.GONE);

        ScrollView scrolluserprofile = (ScrollView)findViewById(R.id.scroll_user_profile);
        scrolluserprofile.setVisibility(View.GONE);

        ScrollView scrolluserreport = (ScrollView)findViewById(R.id.scroll_user_report);
        scrolluserreport.setVisibility(View.GONE);

        ScrollView scrollsignup = (ScrollView)findViewById(R.id.scroll_signup);
        scrollsignup.setVisibility(View.GONE);

        ScrollView layoutlogin = (ScrollView)findViewById(R.id.layout_login);
        layoutlogin.setVisibility(View.GONE);

        LinearLayout layoutfaq = (LinearLayout)findViewById(R.id.layout_faq);
        layoutfaq.setVisibility(View.GONE);

        LinearLayout layout_detailu = (LinearLayout)findViewById(R.id.layout_detailu);
        layout_detailu.setVisibility(View.GONE);


    }




    CheckBox checkbox_submit;
    EditText editText_submit;

    public void submit_report() {
        checkbox_submit = (CheckBox) findViewById(R.id.checkBox_submit);

        editText_submit = (EditText) findViewById(R.id.editText_submit);

        submit_authority = (Spinner) findViewById(R.id.submit_spinner);
        submit_department = (Spinner) findViewById(R.id.submit_spinner2);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.authority, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        submit_authority.setAdapter(arrayAdapter);


        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this, R.array.department, android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        submit_department.setAdapter(arrayAdapter2);


        Button submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String description = editText_submit.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please give some description", Toast.LENGTH_LONG)
                            .show();
                } else

                {
                    String str_auth = submit_authority.getSelectedItem().toString();
                    String str_dept = submit_department.getSelectedItem().toString();

                    submit_username = global_username;
                    if (checkbox_submit.isChecked()) {
                        submit_username = "";
                    }
                    String sub_authority = str_auth + ", " + str_dept + ", " + "BUET";

                    /*Toast.makeText(getApplicationContext(),
                            global_username+ "  "+ sub_authority + "   " + description, Toast.LENGTH_LONG)
                            .show();*/

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Toast.makeText(getApplicationContext(),
                                            "Report Successfully Submitted !!!" +
                                                    "You may submit another !!!", Toast.LENGTH_LONG)
                                            .show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Error :( :(:( :(:(:(", Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Report Successfully Submitted !!" +
                                                "You may submit another !!!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(submit_username, sub_authority, description, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(registerRequest);


                    checkbox_submit.setChecked(false);
                    editText_submit.getText().clear();

                }
            }

        });

        Button reset_sub_button = (Button) findViewById(R.id.reset_sub_button);
        reset_sub_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                checkbox_submit.setChecked(false);
                editText_submit.getText().clear();

            }

        });
    }

 //---------------------------------------------------------Register----------------------------------------------------------------


    EditText edittext_fullname;
    EditText edittext_contact ;
    EditText edittext_username ;
    EditText edittext_password ;
    EditText edittext_cpassword ;

    CheckBox checkbox_authority;

    String sub_authority = "";
    String user_type = "Reporter";


    public void register()
    {
     edittext_fullname = (EditText) findViewById(R.id.edittext_fullname);
     edittext_contact = (EditText) findViewById(R.id.edittext_contact);
     edittext_username = (EditText) findViewById(R.id.edittext_username);
     edittext_password = (EditText) findViewById(R.id.edittext_password);
     edittext_cpassword = (EditText) findViewById(R.id.edittext_cpassword);

    checkbox_authority = (CheckBox) findViewById(R.id.checkbox_authority);


    checkbox_authority.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(checkbox_authority.isChecked())
            {
                FrameLayout frame_ifauthority = (FrameLayout) findViewById(R.id.frame_ifauthority);
                frame_ifauthority.setVisibility(View.VISIBLE);
            }
            else
            {
                FrameLayout frame_ifauthority = (FrameLayout) findViewById(R.id.frame_ifauthority);
                frame_ifauthority.setVisibility(View.GONE);
            }
        }
    });




    Button button_signup = (Button) findViewById(R.id.button_signup);

    button_signup.setOnClickListener(new View.OnClickListener() {

        public void onClick(View view) {
                //String email = inputEmail.getText().toString().trim();
                String et_fullname = edittext_fullname.getText().toString();
                String et_contact = edittext_contact.getText().toString();
                String et_username = edittext_username.getText().toString();
                String et_password = edittext_password.getText().toString();
                String et_cpassword = edittext_cpassword.getText().toString();

                // Check for empty data in the form
                if (et_fullname.isEmpty() ||  et_contact.isEmpty() || et_username.isEmpty() ||  et_password.isEmpty()||  et_cpassword.isEmpty()) {

                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
                else if(!et_password.equals(et_cpassword))
                {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Password Dont Match !!", Toast.LENGTH_LONG)
                            .show();
                }

                else
                {

                    if(checkbox_authority.isChecked())
                    {
                        String str_auth = sp_authority.getSelectedItem().toString();
                        String str_dept = sp_department.getSelectedItem().toString();
                        sub_authority = str_auth + ", " + str_dept + ", " + "BUET";

                        for(int i=0; i<rlength; i++)
                        {
                            if(sub_authority.equalsIgnoreCase(rresponsible[i]))
                            {
                                Toast.makeText(getApplicationContext(),
                                        "Authority Already Exists !!!!!!", Toast.LENGTH_LONG)
                                        .show();
                                return;
                            }


                        }
                        user_type = "Authority";
                        isAuthority = true;
                    }


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    global_authority = sub_authority;
                                    global_username = edittext_username.getText().toString();
                                    isLoggedin = true;

                                    tab4layoutsgone();

                                    listview_login();

                                    LinearLayout layoutlistview_login = (LinearLayout)findViewById(R.id.layout_listview_login);
                                    layoutlistview_login.setVisibility(View.VISIBLE);

                                    edittext_fullname.getText().clear();
                                    edittext_contact.getText().clear() ;
                                    edittext_username.getText().clear() ;
                                    edittext_password.getText().clear() ;
                                    edittext_cpassword.getText().clear();
                                    checkbox_authority.setChecked(false);




                                    Toast.makeText(getApplicationContext(),
                                            "User Successfully Registered !!", Toast.LENGTH_LONG)
                                            .show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Error in Registration ", Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Holllllaaaaaaa", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    };

                    RegisterRequest2 registerRequest2 = new RegisterRequest2(et_username, et_fullname, et_contact, et_password, user_type, sub_authority, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(registerRequest2);






                }

        }

    });

    Button button_sign_reset = (Button) findViewById(R.id.button_sign_reset);
    button_sign_reset.setOnClickListener(new View.OnClickListener() {

        public void onClick(View view) {
            checkbox_authority.setChecked(false);
            edittext_contact.getText().clear();
            edittext_fullname.getText().clear();
            edittext_username.getText().clear();
            edittext_password.getText().clear();
            edittext_cpassword.getText().clear();



        }

    });

}

//-----------------------------------------------------------------Log In--------------------------------------------------------------

    EditText editText_user ;
    EditText editText_pass ;

    public void login()
    {


    editText_user = (EditText) findViewById(R.id.editText_user);
    editText_pass = (EditText) findViewById(R.id.editText_pass);

    Button button_login = (Button) findViewById(R.id.button_login);

    button_login.setOnClickListener(new View.OnClickListener() {

        public void onClick(View view) {
                String username = editText_user.getText().toString();
                String password = editText_pass.getText().toString();
                Boolean flag = false;
                // Check for empty data in the form
                if (username.isEmpty() || password.isEmpty()) {
                    // login user
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    g_register();
                    for(int i=0; i<rlength; i++)
                    {
                        if(username.equals(ruser_id[i]))
                        {
                            if(password.equals(rpassword[i]))
                            {

                                global_username = editText_user.getText().toString();
                                isLoggedin = true;

                                if(!rresponsible[i].equals(""))
                                {
                                    isAuthority = true;
                                    global_authority = rresponsible[i];
                                }
                                else
                                {
                                    isAuthority = false;
                                    global_authority = "";
                                }

                                tab4layoutsgone();

                                listview_login();

                                LinearLayout layoutlistview_login = (LinearLayout)findViewById(R.id.layout_listview_login);
                                layoutlistview_login.setVisibility(View.VISIBLE);

                                flag = true;

                                editText_user.getText().clear(); ;
                                editText_pass.getText().clear();


                                Toast.makeText(getApplicationContext(),
                                        "Login Successful !!!", Toast.LENGTH_LONG)
                                        .show();
                            }

                            else
                            {
                                flag = true;
                                Toast.makeText(getApplicationContext(),
                                        "Wrong Password !!!", Toast.LENGTH_LONG)
                                        .show();
                            }


                            break;

                        }

                    }

                    if(!flag)
                        Toast.makeText(getApplicationContext(),
                                "User Doesnt Exist", Toast.LENGTH_LONG)
                                .show();



                    //----------------------------------JSON--------------------------------------------

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                   // String name = jsonResponse.getString("name");
                                    //int age = jsonResponse.getInt("age");


                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "LOGIN FAILED !!!" +
                                                    "Please enter the credentials correctly!", Toast.LENGTH_LONG)
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        e.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(loginRequest);

                }
        }

    });

}

//-----------------------------------------------------------------COMMENT SUBMIT--------------------------------------------------------------

    CheckBox checkbox_comment;
    EditText editText_comment;
    String description;

    public  void submit_explore_comments()
    {
        checkbox_comment = findViewById(R.id.checkBox_comment);

        editText_comment = findViewById(R.id.editText_comment);

        Button comment_button = (Button) findViewById(R.id.comment_button);
        comment_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                 description = editText_comment.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please give some comment", Toast.LENGTH_LONG)
                            .show();
                } else

                {
                    submit_username = global_username;
                    if (checkbox_comment.isChecked()) {
                        submit_username = "";
                    }
                    /*Toast.makeText(getApplicationContext(),
                            global_username+ "  "+ sub_authority + "   " + description, Toast.LENGTH_LONG)
                            .show();*/

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Toast.makeText(getApplicationContext(),
                                            "Comment Added Successfully !!!" +
                                                    "You may add another !!!", Toast.LENGTH_LONG)
                                            .show();
                                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                                    TableLayout comments_table = (TableLayout) findViewById(R.id.comments_table);



                                    TableRow infrow = new TableRow(getApplicationContext());
                                    View inflatedView = getLayoutInflater().inflate(R.layout.comment, null);


                                    TextView text = (TextView) inflatedView.findViewById(R.id.tv_com_userid);
                                    text.setText(submit_username);

                                    TextView tv_comment_time = (TextView)inflatedView.findViewById(R.id.tv_comment_time);
                                    tv_comment_time.setText(timeStamp);

                                    TextView tv_comment = (TextView)inflatedView.findViewById(R.id.tv_comment);
                                    tv_comment.setText(description);

                                    infrow.addView(inflatedView);
                                    comments_table.addView(infrow);




                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Error :( :(:( :(:(:(", Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Error !!!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    };

                    CommentRequest commentRequest = new CommentRequest(explore_reportid , submit_username, description, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(commentRequest);


                    checkbox_comment.setChecked(false);
                    editText_comment.getText().clear();



                    g_comment();





                }
            }

        });

        Button reset_comment = (Button) findViewById(R.id.reset_comment);
        reset_comment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                checkbox_comment.setChecked(false);
                editText_comment.getText().clear();

            }

        });


    }


    EditText editText_feedback;

    public void givefeedback(final int report_id, final String responsible)
    {
        editText_feedback = findViewById(R.id.editText_feedback);

        Button feedback_button = (Button) findViewById(R.id.feedback_button);
        feedback_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                 description = editText_feedback.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please give some feedback", Toast.LENGTH_LONG)
                            .show();
                } else

                {


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success)
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "FeedBack Added Successfully !!!", Toast.LENGTH_LONG)
                                            .show();


                                    TextView tv_feedback_time = (TextView)findViewById(R.id.tv_feedback_timeu);
                                    TextView tv_feedback = (TextView)findViewById(R.id.tv_feedbacku);

                                    g_feedback();

                                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());



                                    tv_feedback_time.setText(timeStamp);
                                    tv_feedback.setText(description);




                                    TableRow tr_feedback = findViewById(R.id.tr_feedback);
                                    tr_feedback.setVisibility(View.GONE);

                                    TableRow tablerow_feedback = findViewById(R.id.tablerow_feedback);
                                    tablerow_feedback.setVisibility(View.VISIBLE);





                                 }


                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "Error :( :(:( :(:(:(", Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Error !!!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    };

                    FeedbackRequest feedbackRequest = new FeedbackRequest(report_id, responsible, description, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(feedbackRequest);


                    editText_feedback.getText().clear();

                }
            }

        });

        Button reset_feedback = (Button) findViewById(R.id.reset_feedback);
        reset_feedback.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                editText_feedback.getText().clear();

            }

        });

    }

    public native String stringFromJNI();

    void loadReports() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_report,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // String ss[] = new String[100];
                        try {

                            //converting the string to json array object
                            JSONArray jarray = new JSONArray(response);

                            int length = jarray.length();
                            jlength = length;
                            jresponsible = null;
                            jresponsible = new String[length];
                            for (int i = 0; i < length; i++)
                            {
                                //getting product object from json array
                                // JSONObject report = new JSONObject(response);
                                JSONObject report = jarray.getJSONObject(i);

                                jreport_id[i] = report.getInt("report_id");
                                //s[0] = report.getString("report_id");
                                juser_id[i] = report.getString("user_id");
                                jresponsible[i] = report.getString("responsible");
                                jsubmission[i] = report.getString("submission");
                                jdescription[i] = report.getString("description");


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        //Volley.newRequestQueue(this).add(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void g_register() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_report_rreg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // String ss[] = new String[100];
                        try {

                            //converting the string to json array object
                            JSONArray jarray = new JSONArray(response);

                            int length = jarray.length();
                            rlength = length;

                            for (int i = 0; i < length; i++)
                            {
                                //getting product object from json array
                                // JSONObject report = new JSONObject(response);
                                JSONObject report = jarray.getJSONObject(i);

                                runique_id[i] = report.getInt("unique_id");
                                //s[0] = report.getString("report_id");
                                ruser_id[i] = report.getString("user_id");
                                rfull_name[i] = report.getString("full_name");
                                rcontact_no[i] = report.getString("contact_no");
                                rpassword[i] = report.getString("password");
                                ruser_type[i] = report.getString("user_type");

                                rresponsible[i] = report.getString("responsible");
                                rsubmission[i] = report.getString("submission");
                                rupdated_at[i] = report.getString("updated_at");

                                Log.v("fuck", ruser_id[i] );

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("mye", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("myee", error.toString());
                    }
                });

        //adding our stringrequest to queue
        //Volley.newRequestQueue(this).add(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void g_feedback() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_report_feed,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // String ss[] = new String[100];
                        try {

                            //converting the string to json array object
                            JSONArray jarray = new JSONArray(response);

                            int length = jarray.length();
                            flength = length;

                            for (int i = 0; i < length; i++)
                            {
                                //getting product object from json array
                                // JSONObject report = new JSONObject(response);
                                JSONObject report = jarray.getJSONObject(i);

                                freport_id[i] = report.getInt("report_id");
                                //s[0] = report.getString("report_id");
                                fresponsible[i] = report.getString("responsible");
                                fsubmission[i] = report.getString("submission");
                                ffeedback[i] = report.getString("feedback");

                                Log.v("hee", ffeedback[i]);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        //Volley.newRequestQueue(this).add(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void g_comment() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_report_comment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // String ss[] = new String[100];
                        try {

                            //converting the string to json array object
                            JSONArray jarray = new JSONArray(response);

                            int length = jarray.length();
                            clength = length;

                            for (int i = 0; i < length; i++)
                            {
                                //getting product object from json array
                                // JSONObject report = new JSONObject(response);
                                JSONObject report = jarray.getJSONObject(i);

                                creport_id[i] = report.getInt("report_id");
                                //s[0] = report.getString("report_id");
                                cuser_id[i] = report.getString("user_id");
                                csubmission[i] = report.getString("submission");
                                ccomments[i] = report.getString("comments");

                                Log.v("comm", ccomments[i]);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        //Volley.newRequestQueue(this).add(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





    void jsonSubmit(final String user_id,final String responsible,final String description) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_submit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // String ss[] = new String[100];
                        try {
                           /* Toast.makeText(getApplicationContext(),
                                    "hellllooooooooooooo", Toast.LENGTH_LONG)
                                    .show();*/
                                JSONObject report = new JSONObject(response);

                                report.put("user_id", user_id);
                                report.put("responsible", responsible);
                                report.put("description", description);

                                Boolean success = report.getBoolean("success");

                                if(success)
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Successful !!!!!!!!!!!!!!!", Toast.LENGTH_LONG)
                                            .show();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Error :( :(:( :(:(:(", Toast.LENGTH_LONG)
                                            .show();

                                }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        //Volley.newRequestQueue(this).add(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    View.OnClickListener tablerowOnClickListener = new View.OnClickListener()
    {
        public void onClick(View view)
        {
            for (int i = 0; i < table_explore.getChildCount(); i++)
            {
                View row = table_explore.getChildAt(i);
                if (row == view)
                {
                    TableRow t = (TableRow)view;

                    TextView tv0 = (TextView) t.getChildAt(0);
                    String text0 = tv0.getText().toString();
                    TextView tv_reportid = (TextView)findViewById(R.id.tv_reportid);
                    tv_reportid.setText(text0);
                    explore_reportid = Integer.parseInt(text0);

                    TextView tv1 = (TextView) t.getChildAt(1);
                    String text1 = tv1.getText().toString();
                    TextView tv_report_time = (TextView)findViewById(R.id.tv_report_time);
                    tv_report_time.setText(text1);

                    TextView tv2 = (TextView) t.getChildAt(2);
                    String text2 = tv2.getText().toString();
                    TextView tv_description = (TextView)findViewById(R.id.tv_description);
                    tv_description.setText(text2);

                    TextView tv3 = (TextView) t.getChildAt(3);
                    String text3 = tv3.getText().toString();
                    TextView tv_authority = (TextView)findViewById(R.id.tv_authority);
                    tv_authority.setText(text3);

                    TextView tv_user = (TextView)findViewById(R.id.tv_user);
                    tv_user.setText(juser_id[i]);

                    TextView tv_authorityid = (TextView)findViewById(R.id.tv_authorityid);
                    tv_authorityid.setText(text3);


                    Boolean flag = false;
                    int index=-1;

                    g_feedback();
                    g_comment();

                    for(int j=0; j<flength; j++)
                    {
                        if(freport_id[j] == explore_reportid)
                        {
                            flag = true;
                            index = j;
                            break;
                        }

                    }


                    TextView tv_feedback_time = (TextView)findViewById(R.id.tv_feedback_time);
                    TextView tv_feedback = (TextView)findViewById(R.id.tv_feedback);

                    if(!flag)
                    {

                        tv_feedback_time.setText("N/A");
                        tv_feedback.setText("N/A");

                    }

                    else
                    {
                        tv_feedback_time.setText(fsubmission[index]);
                        tv_feedback.setText(ffeedback[index]);
                    }

                    //LayoutInflater l = getLayoutInflater();

                    g_comment();




                    TableLayout comments_table = (TableLayout) findViewById(R.id.comments_table);
                    comments_table.removeAllViews();

                    //View inflatedView = getLayoutInflater().inflate(R.layout.comment, null);


                    comments_table = (TableLayout) findViewById(R.id.comments_table);

                    for(int k=0; k<clength; k++)
                    {
                        if(explore_reportid == creport_id[k])
                        {

                            TableRow infrow = new TableRow(getApplicationContext());
                            View inflatedView = getLayoutInflater().inflate(R.layout.comment, null);


                            TextView text = (TextView) inflatedView.findViewById(R.id.tv_com_userid);
                            text.setText(cuser_id[k]);

                            TextView tv_comment_time = (TextView)inflatedView.findViewById(R.id.tv_comment_time);
                            tv_comment_time.setText(csubmission[k]);

                            TextView tv_comment = (TextView)inflatedView.findViewById(R.id.tv_comment);
                            tv_comment.setText(ccomments[k]);

                            infrow.addView(inflatedView);
                            comments_table.addView(infrow);
                        }
                    }




                    LinearLayout layout_explore = (LinearLayout)findViewById(R.id.layout_explore);
                    layout_explore.setVisibility(View.GONE);

                    LinearLayout layout_detail = (LinearLayout)findViewById(R.id.layout_detail);
                    layout_detail.setVisibility(View.VISIBLE);

                    LinearLayout layout_search = (LinearLayout)findViewById(R.id.layout_search);
                    layout_search.setVisibility(View.GONE);


                }
            }
            //...
        }
    };

    View.OnClickListener tablerowOnClickListener_user = new View.OnClickListener()
    {
        public void onClick(View view)
        {
            tablelayout_user = (TableLayout) findViewById(R.id.tablelayout_user);
            for (int i = 0; i < tablelayout_user.getChildCount(); i++)
            {
                View row = tablelayout_user.getChildAt(i);
                if (row == view)
                {
                    TableRow t = (TableRow)view;

                    TextView tv0 = (TextView) t.getChildAt(0);
                    String text0 = tv0.getText().toString();
                    TextView tv_reportid = (TextView)findViewById(R.id.tv_reportidu);
                    tv_reportid.setText(text0);
                    explore_reportid = Integer.parseInt(text0);

                    TextView tv1 = (TextView) t.getChildAt(1);
                    String text1 = tv1.getText().toString();
                    TextView tv_report_time = (TextView)findViewById(R.id.tv_report_timeu);
                    tv_report_time.setText(text1);

                    TextView tv2 = (TextView) t.getChildAt(2);
                    String text2 = tv2.getText().toString();
                    TextView tv_description = (TextView)findViewById(R.id.tv_descriptionu);
                    tv_description.setText(text2);

                    TextView tv3 = (TextView) t.getChildAt(3);
                    String text3 = tv3.getText().toString();
                    TextView tv_authority = (TextView)findViewById(R.id.tv_authorityu);
                    tv_authority.setText(text3);

                    TextView tv_user = (TextView)findViewById(R.id.tv_useru);
                    tv_user.setText(juser_id[i]);


                    TextView tv_authorityid = (TextView)findViewById(R.id.tv_authorityidu);
                    tv_authorityid.setText(text3);


                    Boolean flag = false;
                    int index=-1;

                    for(int j=0; j<flength; j++)
                    {
                        if(freport_id[j] == explore_reportid)
                        {
                            flag = true;
                            index = j;
                            break;
                        }

                    }


                    TextView tv_feedback_time = (TextView)findViewById(R.id.tv_feedback_timeu);
                    TextView tv_feedback = (TextView)findViewById(R.id.tv_feedbacku);

                    if(!flag)
                    {

                        tv_feedback_time.setText("N/A");
                        tv_feedback.setText("N/A");

                    }

                    else
                    {
                        tv_feedback_time.setText(fsubmission[index]);
                        tv_feedback.setText(ffeedback[index]);
                    }


                    g_comment();




                    TableLayout comments_table = (TableLayout) findViewById(R.id.comments_tableu);
                    comments_table.removeAllViews();

                    //View inflatedView = getLayoutInflater().inflate(R.layout.comment, null);


                    comments_table = (TableLayout) findViewById(R.id.comments_tableu);

                    for(int k=0; k<clength; k++)
                    {
                        if(explore_reportid == creport_id[k])
                        {

                            TableRow infrow = new TableRow(getApplicationContext());
                            View inflatedView = getLayoutInflater().inflate(R.layout.comment, null);


                            TextView text = (TextView) inflatedView.findViewById(R.id.tv_com_userid);
                            text.setText(cuser_id[k]);

                            TextView tv_comment_time = (TextView)inflatedView.findViewById(R.id.tv_comment_time);
                            tv_comment_time.setText(csubmission[k]);

                            TextView tv_comment = (TextView)inflatedView.findViewById(R.id.tv_comment);
                            tv_comment.setText(ccomments[k]);

                            infrow.addView(inflatedView);
                            comments_table.addView(infrow);
                        }
                    }



                    ScrollView scrolluserreport = (ScrollView)findViewById(R.id.scroll_user_report);
                    scrolluserreport.setVisibility(View.GONE);

                    LinearLayout layout_detailu = (LinearLayout)findViewById(R.id.layout_detailu);
                    layout_detailu.setVisibility(View.VISIBLE);





                }
            }
            //...
        }
    };

    View.OnClickListener tablerowOnClickListener_authority = new View.OnClickListener()
    {
        public void onClick(View view)
        {
            tablelayout_user = (TableLayout) findViewById(R.id.tablelayout_user);
            for (int i = 0; i < tablelayout_user.getChildCount(); i++)
            {
                View row = tablelayout_user.getChildAt(i);
                if (row == view)
                {
                    TableRow t = (TableRow)view;

                    TextView tv0 = (TextView) t.getChildAt(0);
                    String text0 = tv0.getText().toString();
                    TextView tv_reportid = (TextView)findViewById(R.id.tv_reportidu);
                    tv_reportid.setText(text0);
                    explore_reportid = Integer.parseInt(text0);

                    TextView tv1 = (TextView) t.getChildAt(1);
                    String text1 = tv1.getText().toString();
                    TextView tv_report_time = (TextView)findViewById(R.id.tv_report_timeu);
                    tv_report_time.setText(text1);

                    TextView tv2 = (TextView) t.getChildAt(2);
                    String text2 = tv2.getText().toString();
                    TextView tv_description = (TextView)findViewById(R.id.tv_descriptionu);
                    tv_description.setText(text2);

                    TextView tv3 = (TextView) t.getChildAt(3);
                    String text3 = tv3.getText().toString();
                    TextView tv_authority = (TextView)findViewById(R.id.tv_authorityu);
                    tv_authority.setText(text3);

                    TextView tv_user = (TextView)findViewById(R.id.tv_useru);
                    tv_user.setText(juser_id[i]);

                    TextView tv_authorityid = (TextView)findViewById(R.id.tv_authorityidu);
                    tv_authorityid.setText(text3);




                    ScrollView scrolluserreport = (ScrollView)findViewById(R.id.scroll_user_report);
                    scrolluserreport.setVisibility(View.GONE);

                    LinearLayout layout_detailu = (LinearLayout)findViewById(R.id.layout_detailu);
                    layout_detailu.setVisibility(View.VISIBLE);


                    Boolean flag = false;
                    int index=-1;

                    for(int j=0; j<flength; j++)
                    {
                        if(freport_id[j] == explore_reportid)
                        {
                            flag = true;
                            index = j;
                            break;
                        }

                    }

                    if(!flag)
                    {
                        TableRow tr_feedback = findViewById(R.id.tr_feedback);
                        tr_feedback.setVisibility(View.VISIBLE);

                        TableRow tablerow_feedback = findViewById(R.id.tablerow_feedback);
                        tablerow_feedback.setVisibility(View.GONE);

                        givefeedback(explore_reportid, text3);
                    }

                    else
                    {

                        TableRow tr_feedback = findViewById(R.id.tr_feedback);
                        tr_feedback.setVisibility(View.GONE);

                        TableRow tablerow_feedback = findViewById(R.id.tablerow_feedback);
                        tablerow_feedback.setVisibility(View.VISIBLE);


                        TextView tv_feedback_time = (TextView)findViewById(R.id.tv_feedback_timeu);
                        TextView tv_feedback = (TextView)findViewById(R.id.tv_feedbacku);

                        tv_feedback_time.setText(fsubmission[index]);
                        tv_feedback.setText(ffeedback[index]);
                    }


                    g_comment();




                    TableLayout comments_table = (TableLayout) findViewById(R.id.comments_tableu);
                    comments_table.removeAllViews();

                    //View inflatedView = getLayoutInflater().inflate(R.layout.comment, null);


                    comments_table = (TableLayout) findViewById(R.id.comments_tableu);

                    for(int k=0; k<clength; k++)
                    {
                        if(explore_reportid == creport_id[k])
                        {

                            TableRow infrow = new TableRow(getApplicationContext());
                            View inflatedView = getLayoutInflater().inflate(R.layout.comment, null);


                            TextView text = (TextView) inflatedView.findViewById(R.id.tv_com_userid);
                            text.setText(cuser_id[k]);

                            TextView tv_comment_time = (TextView)inflatedView.findViewById(R.id.tv_comment_time);
                            tv_comment_time.setText(csubmission[k]);

                            TextView tv_comment = (TextView)inflatedView.findViewById(R.id.tv_comment);
                            tv_comment.setText(ccomments[k]);

                            infrow.addView(inflatedView);
                            comments_table.addView(infrow);
                        }
                    }


                }
            }
            //...
        }
    };
}
