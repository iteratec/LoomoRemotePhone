package de.iteratec.slab.segway.remote.phone.fragment.base;

import de.iteratec.slab.segway.remote.phone.service.ConnectionService;

/**
 * Created by mss on 17.01.18.
 */

public abstract class PreferenceFragment extends android.preference.PreferenceFragment implements RemoteFragmentInterface {

    protected int fragmentId;
    protected String fragmentTitle;

    public ConnectionService getLoomoService() {
        return ConnectionService.getInstance();
    }

    public String getTitle() {
        return fragmentTitle;
    }

    public void setTitle(String title) {
        fragmentTitle = title;
    }

    public int getFragmendId() {
        return fragmentId;
    }

    public boolean isFragmentId(int id) {
        return id == fragmentId;
    }

}
