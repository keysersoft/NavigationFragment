package com.dmcapps.navigationfragment.common.interfaces;

import android.os.Bundle;

import com.dmcapps.navigationfragment.common.core.NavigationSettings;

import java.io.Serializable;

/**
 * Created by dcarmo on 2016-02-25.
 */
public interface Stack extends Serializable {

    Navigation pushFragment(NavigationManager navigationManager, Navigation navFragment);

    Navigation pushFragment(NavigationManager navigationManager, Navigation navFragment, NavigationSettings settings);

    Navigation popFragment(NavigationManager navigationManager);

    Navigation popFragment(NavigationManager navigationManager, NavigationSettings settings);

    void clearNavigationStackToPosition(NavigationManager navigationManager, int stackPosition);

    Navigation getFragmentAtIndex(NavigationManager navigationManager, int index);

}
