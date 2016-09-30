package com.dmcapps.navigationfragment.support.v7.manager.core.lifecycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.dmcapps.navigationfragment.common.interfaces.Config;
import com.dmcapps.navigationfragment.common.core.CofigManager;
import com.dmcapps.navigationfragment.common.interfaces.State;

/**
 * Created by dcarmo on 2016-02-24.
 */
public class MasterDetailLifecycleManager implements Lifecycle {

    private static final int MASTER_DETAIL_PHONE_MIN_ACTION_SIZE = 1;
    private static final int MASTER_DETAIL_TABLET_MIN_ACTION_SIZE = 2;

    public void onResume(NavigationManager navigationManager) {
        Config config = navigationManager.getConfig();
        State state = navigationManager.getState();

        FragmentManager childFragManager = NavigationManagerUtils.getSupportFragmentManager(navigationManager);
        FragmentTransaction childFragTrans = childFragManager.beginTransaction();
        // No Fragments have been added. Attach the master and detail.
        if (state.getStack().size() == 0) {
            if (state.isTablet()) {
                // Add in the new fragment that we are presenting and add it's navigation tag to the stack.
                childFragTrans.add(R.id.navigation_manager_container_master, (Fragment) config.getMasterFragment(), config.getMasterFragment().getNavTag());
                navigationManager.addToStack(config.getMasterFragment());
            }
            else {
                navigationManager.pushFragment(config.getMasterFragment());
            }
            childFragTrans.commit();

            if (state.isTablet()) {
                navigationManager.pushFragment(config.getDetailFragment());
            }

            config.nullifyInitialFragments();
        }
        // Fragments are in the stack, resume at the top.
        else {
            childFragTrans.setCustomAnimations(CofigManager.NO_ANIMATION, CofigManager.NO_ANIMATION);
            if (state.isTablet()) {
                childFragTrans.attach(childFragManager.findFragmentByTag(state.getStack().firstElement()));
            }
            childFragTrans.attach(childFragManager.findFragmentByTag(state.getStack().peek()));
            childFragTrans.commit();
        }

        if (navigationManager.getActivity() != null) {
            navigationManager.getActivity().invalidateOptionsMenu();
        }
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

        navigationManager.getConfig().setMinStackSize(navigationManager.getState().isTablet() ? MASTER_DETAIL_TABLET_MIN_ACTION_SIZE : MASTER_DETAIL_PHONE_MIN_ACTION_SIZE);
        navigationManager.getConfig().setPushContainerId(R.id.navigation_manager_fragment_container);
    }

    @Override
    public void onPause(NavigationManager navigationManager) {
        FragmentManager childFragManager = NavigationManagerUtils.getSupportFragmentManager(navigationManager);
        FragmentTransaction childFragTrans = childFragManager.beginTransaction();
        childFragTrans.setCustomAnimations(CofigManager.NO_ANIMATION, CofigManager.NO_ANIMATION);
        if (navigationManager.getState().isTablet()) {
            childFragTrans.detach(childFragManager.findFragmentByTag(navigationManager.getState().getStack().firstElement()));
        }
        childFragTrans.detach(childFragManager.findFragmentByTag(navigationManager.getState().getStack().peek()));
        childFragTrans.commit();

    }

}
