package com.google.firebase.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.quickstart.database.fragment.MinhasConsultasUsuarioFragment;
import com.google.firebase.quickstart.database.fragment.MinhasTopConsultasUsuarioFragment;
import com.google.firebase.quickstart.database.fragment.RecentesConsultasUsuarioFragment;

public class  MainActivityUsuario extends BaseActivity {

    private static final String TAG = "MainActivityUsuario";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);

        // Cria o adaptador com as Tabs
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new MinhasConsultasUsuarioFragment(),
                    new RecentesConsultasUsuarioFragment(),
                    new MinhasTopConsultasUsuarioFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_top_posts)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(MainActivityUsuario.this, TelaPrincipalUsuarioActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void onBackPressed()  {

    }
}
