package com.google.firebase.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.quickstart.database.fragment.MinhasConsultasFragment;
import com.google.firebase.quickstart.database.fragment.MinhasTopConsultasFragment;
import com.google.firebase.quickstart.database.fragment.RecentesConsultasFragment;

public class  MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cria o adaptador com as Tabs
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentesConsultasFragment(),
                    new MinhasConsultasFragment(),
                    new MinhasTopConsultasFragment(),
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
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.btnNovaConsulta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NovaConsultaActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.action_buscas) {
            startActivity(new Intent(this, BuscasActivity.class));
            finish();
        }

        if (i == R.id.action_config) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
            finish();
        }

        if (i == R.id.action_escolha) {
            startActivity(new Intent(this, EscolhaLoginActivity.class));
            finish();
        }

        if (i == R.id.action_sair) {
            finish();
        }

        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return onContextItemSelected(item);
    }
    public void onBackPressed()  {

    }
}
