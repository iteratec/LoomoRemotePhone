package de.iteratec.slab.segway.remote.phone.fragment.base;

import android.app.Fragment;

import de.iteratec.slab.segway.remote.phone.service.ConnectionService;

/**
 * Created by abr on 19.12.17.
 */

public abstract class RemoteFragment extends Fragment implements RemoteFragmentInterface {

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
