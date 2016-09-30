package com.dmcapps.navigationfragment.support.v7.manager.core.micromanagers.lifecycle;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmcapps.navigationfragment.R;
import com.dmcapps.navigationfragment.common.interfaces.Config;
import com.dmcapps.navigationfragment.common.interfaces.Lifecycle;
import com.dmcapps.navigationfragment.common.interfaces.State;
import com.dmcapps.navigationfragment.support.v7.manager.core.NavigationManagerFragment;
import com.dmcapps.navigationfragment.common.micromanagers.CofigManager;

/**
 * Created by dcarmo on 2016-02-24.
 */
public class StackLifecycleManager implements Lifecycle {

    private static final int SINGLE_STACK_MIN_ACTION_SIZE = 1;

    @Override
    public void onResume(NavigationManagerFragment navMgrFragment, State state, Config config) {
        // No Fragments have been added. Attach the root.
        if (state.getStack().size() == 0) {
            navMgrFragment.pushFragment(config.getRootFragment());
        }
        // Fragments are in the stack, resume at the top.
        else {
            FragmentManager childFragManager = navMgrFragment.getNavigationFragmentManager();
            FragmentTransaction childFragTrans = childFragManager.beginTransaction();
            childFragTrans.setCustomAnimations(CofigManager.NO_ANIMATION, CofigManager.NO_ANIMATION);
            childFragTrans.attach(childFragManager.findFragmentByTag(state.getStack().peek()));
            childFragTrans.commit();
        }

        config.nullifyInitialFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, State state, Config config) {
        state.isTablet(view.findViewById(R.id.navigation_manager_tablet_land) != null
                || view.findViewById(R.id.navigation_manager_tablet_portrait) != null);
        state.isPortrait(view.findViewById(R.id.navigation_manager_phone_portrait) != null
                || view.findViewById(R.id.navigation_manager_tablet_portrait) != null);

        config.setMinStackSize(SINGLE_STACK_MIN_ACTION_SIZE);
        config.setPushContainerId(R.id.navigation_manager_fragment_container);

        if (view.findViewById(R.id.navigation_manager_container_master) != null) {
            view.findViewById(R.id.navigation_manager_container_master).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause(NavigationManagerFragment navMgrFragment, State state) {
        FragmentManager childFragManager = navMgrFragment.getNavigationFragmentManager();
        FragmentTransaction childFragTrans = childFragManager.beginTransaction();
        childFragTrans.setCustomAnimations(CofigManager.NO_ANIMATION, CofigManager.NO_ANIMATION);
        childFragTrans.detach(childFragManager.findFragmentByTag(state.getStack().peek()));
        childFragTrans.commit();
    }

}
