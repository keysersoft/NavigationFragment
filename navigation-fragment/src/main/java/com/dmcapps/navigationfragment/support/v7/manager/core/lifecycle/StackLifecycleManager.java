package com.dmcapps.navigationfragment.support.v7.manager.core.lifecycle;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmcapps.navigationfragment.R;
import com.dmcapps.navigationfragment.common.helpers.utils.NavigationManagerUtils;
import com.dmcapps.navigationfragment.common.helpers.utils.ObjectUtils;
import com.dmcapps.navigationfragment.common.interfaces.Lifecycle;
import com.dmcapps.navigationfragment.common.interfaces.NavigationManager;
import com.dmcapps.navigationfragment.common.core.CofigManager;

/**
 * Created by dcarmo on 2016-02-24.
 */
public class StackLifecycleManager implements Lifecycle {

    private static final int SINGLE_STACK_MIN_ACTION_SIZE = 1;

    @Override
    public void onResume(NavigationManager navigationManager) {
        // No Fragments have been added. Attach the root.
        if (navigationManager.getState().getStack().size() == 0) {
            navigationManager.pushFragment(navigationManager.getConfig().getRootFragment());
        }
        // Fragments are in the stack, resume at the top.
        else {
            FragmentManager childFragManager = NavigationManagerUtils.getSupportFragmentManager(navigationManager);
            FragmentTransaction childFragTrans = childFragManager.beginTransaction();
            childFragTrans.setCustomAnimations(CofigManager.NO_ANIMATION, CofigManager.NO_ANIMATION);
            childFragTrans.attach(childFragManager.findFragmentByTag(navigationManager.getState().getStack().peek()));
            childFragTrans.commit();
        }

        navigationManager.getConfig().nullifyInitialFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, NavigationManager navigationManager) {
        navigationManager.getState().isTablet(view.findViewById(R.id.navigation_manager_tablet_land) != null
                || view.findViewById(R.id.navigation_manager_tablet_portrait) != null);
        navigationManager.getState().isPortrait(view.findViewById(R.id.navigation_manager_phone_portrait) != null
                || view.findViewById(R.id.navigation_manager_tablet_portrait) != null);

        navigationManager.getConfig().setMinStackSize(SINGLE_STACK_MIN_ACTION_SIZE);
        navigationManager.getConfig().setPushContainerId(R.id.navigation_manager_fragment_container);

        if (view.findViewById(R.id.navigation_manager_container_master) != null) {
            view.findViewById(R.id.navigation_manager_container_master).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause(NavigationManager navigationManager) {
        FragmentManager childFragManager = NavigationManagerUtils.getSupportFragmentManager(navigationManager);
        FragmentTransaction childFragTrans = childFragManager.beginTransaction();
        childFragTrans.setCustomAnimations(CofigManager.NO_ANIMATION, CofigManager.NO_ANIMATION);
        childFragTrans.detach(childFragManager.findFragmentByTag(navigationManager.getState().getStack().peek()));
        childFragTrans.commit();
    }
}
