package net.redfrench.bootcamplocator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import net.redfrench.bootcamplocator.fragments.MainFragment;

public class MapsActivity extends FragmentActivity {


    private static MapsActivity mainActivity;

    public static void setMainActivity(MapsActivity mainActivity) {
        MapsActivity.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapsActivity.setMainActivity(this);  // now have access to mainActivity from anywhere


//        FragmentManager fragMgr = getSupportFragmentManager();  // opted not to use a variable for frag manger as done in DevRadio
        MainFragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.container_main);  // cast returned fragment into a MainFragment
                                                                                                   // because a regular fragment will be returned

        if (mainFragment == null) {  // if the fragment has previously loaded, it will be in memory. Otherwise, fragment will be null.
            mainFragment = MainFragment.newInstance(); // new MainFragment will be created in newInstance method
            getSupportFragmentManager().beginTransaction()  // anytime you work with the FragmentManager, you begin a transaction
                    .add(R.id.container_main, mainFragment)  // put mainFragment in the 'container_main' fragment
                    .commit();  // always end by committing
        }

    }

}
