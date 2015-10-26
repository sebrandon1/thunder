package network.thunder.core.communication.objects.p2p.sync;

import network.thunder.core.communication.objects.p2p.P2PDataObject;
import network.thunder.core.etc.Tools;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ChannelStatusObject implements P2PDataObject {

    public byte[] pubkeyA;
    public byte[] pubkeyB;

    public byte[] infoA;
    public byte[] infoB;

    public byte[] signatureA;
    public byte[] signatureB;

    public int timestamp;

    public ChannelStatusObject () {
    }

    public ChannelStatusObject (ResultSet set) throws SQLException {
        this.pubkeyA = set.getBytes("nodes_a_table.pubkey");
        this.pubkeyB = set.getBytes("nodes_b_table.pubkey");
        this.infoA = set.getBytes("info_a");
        this.infoB = set.getBytes("info_a");
        this.signatureA = set.getBytes("signature_a");
        this.signatureB = set.getBytes("signature_b");
        this.timestamp = set.getInt("timestamp");
    }

    @Override
    public void verify () {
    }

    @Override
    public long getHashAsLong () {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.put(Tools.hashSecret(this.getData()), 0, 8);
        byteBuffer.flip();
        return Math.abs(byteBuffer.getLong());
    }

    public byte[] getHash() {
        byte[] hash = new byte[20];
        byte[] t = Tools.hashSecret(this.getData());
        System.arraycopy(t, 0, hash, 0, 20);
        return hash;
    }

    private byte[] getData () {
        //TODO: Have some proper summary here..
        ByteBuffer byteBuffer = ByteBuffer.allocate(pubkeyA.length + pubkeyB.length + infoA.length + infoB.length + signatureA.length + signatureB.length + 4);

        byteBuffer.put(pubkeyA);
        byteBuffer.put(pubkeyB);
        byteBuffer.put(infoA);
        byteBuffer.put(infoB);
        byteBuffer.put(signatureA);
        byteBuffer.put(signatureB);
        byteBuffer.putInt(timestamp);

        return byteBuffer.array();
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChannelStatusObject that = (ChannelStatusObject) o;

        if (timestamp != that.timestamp) {
            return false;
        }
        if (!Arrays.equals(pubkeyA, that.pubkeyA)) {
            return false;
        }
        if (!Arrays.equals(pubkeyB, that.pubkeyB)) {
            return false;
        }
        if (!Arrays.equals(infoA, that.infoA)) {
            return false;
        }
        if (!Arrays.equals(infoB, that.infoB)) {
            return false;
        }
        if (!Arrays.equals(signatureA, that.signatureA)) {
            return false;
        }
        return Arrays.equals(signatureB, that.signatureB);

    }

    @Override
    public int hashCode () {
        int result = pubkeyA != null ? Arrays.hashCode(pubkeyA) : 0;
        result = 31 * result + (pubkeyB != null ? Arrays.hashCode(pubkeyB) : 0);
        result = 31 * result + (infoA != null ? Arrays.hashCode(infoA) : 0);
        result = 31 * result + (infoB != null ? Arrays.hashCode(infoB) : 0);
        result = 31 * result + (signatureA != null ? Arrays.hashCode(signatureA) : 0);
        result = 31 * result + (signatureB != null ? Arrays.hashCode(signatureB) : 0);
        result = 31 * result + timestamp;
        return result;
    }

    public static ChannelStatusObject getRandomObject () {
        ChannelStatusObject obj = new ChannelStatusObject();

        obj.pubkeyA = Tools.getRandomByte(33);
        obj.pubkeyB = Tools.getRandomByte(33);

        obj.infoA = Tools.getRandomByte(60);
        obj.infoB = Tools.getRandomByte(60);

        obj.timestamp = Tools.currentTime();

        obj.signatureA = Tools.getRandomByte(65);
        obj.signatureB = Tools.getRandomByte(65);

        return obj;
    }
}
