package nl.tue.moviematch;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Global variables
    DrawerLayout drawerLayout; // represents the drawerLayout
    ActionBarDrawerToggle mToggle; // toggler for the actionBar
    Toolbar mToolbar; // the toolbar
    NavigationView navigationView; // the navigation view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view
        setContentView(R.layout.activity_main);

        // Set the toolbar
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        // Set the drawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        // Add a drawerListener with the toggle to the drawerLayout
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        // Set the navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Get the id
        int id = item.getItemId();
        // Creating intents for the different items in the drawerLayout
        switch (id) {
            // When clicked on the Home label
            case R.id.nav_home:
                // Create a new intent for the HomeActivity
                Intent h = new Intent(MainActivity.this, HomeActivity.class);
                // Start the HomeActivity
                startActivity(h);
                break;
            case R.id.nav_theater:
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_settings:
                Intent g = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(g);
                break;
            case R.id.nav_help:
                Intent s = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(s);
            case R.id.nav_about:
                Intent t = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(t);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
