package de.iteratec.slab.segway.remote.phone.fragment.base;

import de.iteratec.slab.segway.remote.phone.service.ConnectionService;

/**
 * Created by mss on 23.02.18.
 */

public interface RemoteFragmentInterface {
    ConnectionService getLoomoService();

    String getTitle();

    int getFragmendId();

    boolean isFragmentId(int id);

    void setTitle(String title);
}
