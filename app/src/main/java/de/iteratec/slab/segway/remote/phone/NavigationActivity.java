package de.iteratec.slab.segway.remote.phone;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import de.iteratec.slab.segway.remote.phone.fragment.RawControlFragment;
import de.iteratec.slab.segway.remote.phone.fragment.base.FragmentFactory;
import de.iteratec.slab.segway.remote.phone.fragment.base.RemoteFragmentInterface;
import de.iteratec.slab.segway.remote.phone.service.ConnectionCallback;
import de.iteratec.slab.segway.remote.phone.service.ConnectionService;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallback {


    private static final String TAG = NavigationActivity.class.getName();

    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.navigation_fragment_title_raw);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set initial fragment if none is set
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new RawControlFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            super.finish();
        }
        RemoteFragmentInterface fragment = FragmentFactory.getFragment(this, id);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, (Fragment) fragment).commit();
        toolbar.setTitle(fragment.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "Connected to service. Redirecting to NavigationActivity");
            ConnectionService.setInstance(((ConnectionService.LocalBinder) iBinder).getService());
            ConnectionService.getInstance().registerCallback(NavigationActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "Disconnected from service");
            ConnectionService.getInstance().unregisterCallback(NavigationActivity.this);
            ConnectionService.setInstance(null);
        }
    };


    private void disconnectFromLoomo() {
        ConnectionService.getInstance().disconnectFromLoomo();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "onDisconnected called");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectFromLoomo();
        ConnectionService.getInstance().unregisterCallback(NavigationActivity.this);
        unbindService(serviceConnection);
    }
}
