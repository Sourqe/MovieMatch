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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

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

        // Get the suppport action bar and enable it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkergreenish));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // When an item is selected and the menu is toggled
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
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;
            case R.id.nav_settings:
                Intent g = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(g);
                break;
            case R.id.nav_help:
                Intent s = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(s);
                break;
            case R.id.nav_about:
                Intent t = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(t);
                break;
        }
        // Set the drawerLayout to drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        // Close the drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
