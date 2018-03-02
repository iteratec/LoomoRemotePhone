package de.iteratec.slab.segway.remote.phone.service;

/**
 * Created by mss on 05.01.18.
 *
 * This is used so a Fragment can register itself with the ConnectionService to declare that it wishes
 * to receive incoming ByteMessages. This is needed since the Service does not know which Fragment is
 * currently active.
 */
public interface ByteMessageReceiver {

    void handleByteMessage(byte[] message);
}
