/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.distributed.near;

import org.apache.ignite.lang.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.kernal.processors.cache.*;
import org.gridgain.grid.kernal.processors.cache.distributed.*;
import org.gridgain.grid.util.direct.*;
import org.gridgain.grid.util.tostring.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.*;
import java.util.*;

/**
 * Near transaction finish request.
 */
public class GridNearTxFinishRequest<K, V> extends GridDistributedTxFinishRequest<K, V> {
    /** */
    private static final long serialVersionUID = 0L;

    /** Mini future ID. */
    private IgniteUuid miniId;

    /** Explicit lock flag. */
    private boolean explicitLock;

    /** Topology version. */
    private long topVer;

    /** Subject ID. */
    @GridDirectVersion(1)
    private UUID subjId;

    /** Task name hash. */
    @GridDirectVersion(2)
    private int taskNameHash;

    /**
     * Empty constructor required for {@link Externalizable}.
     */
    public GridNearTxFinishRequest() {
        // No-op.
    }

    /**
     * @param futId Future ID.
     * @param xidVer Transaction ID.
     * @param threadId Thread ID.
     * @param commit Commit flag.
     * @param invalidate Invalidate flag.
     * @param explicitLock Explicit lock flag.
     * @param topVer Topology version.
     * @param baseVer Base version.
     * @param committedVers Committed versions.
     * @param rolledbackVers Rolled back versions.
     * @param txSize Expected transaction size.
     * @param writeEntries Write entries.
     * @param recoverEntries Recover entries.
     */
    public GridNearTxFinishRequest(
        IgniteUuid futId,
        GridCacheVersion xidVer,
        long threadId,
        boolean commit,
        boolean invalidate,
        boolean syncCommit,
        boolean syncRollback,
        boolean explicitLock,
        long topVer,
        GridCacheVersion baseVer,
        Collection<GridCacheVersion> committedVers,
        Collection<GridCacheVersion> rolledbackVers,
        int txSize,
        Collection<GridCacheTxEntry<K, V>> writeEntries,
        Collection<GridCacheTxEntry<K, V>> recoverEntries,
        @Nullable UUID subjId,
        int taskNameHash) {
        super(xidVer, futId, null, threadId, commit, invalidate, syncCommit, syncRollback, baseVer, committedVers,
            rolledbackVers, txSize, writeEntries, recoverEntries, null);

        this.explicitLock = explicitLock;
        this.topVer = topVer;
        this.subjId = subjId;
        this.taskNameHash = taskNameHash;
    }

    /**
     * @return Explicit lock flag.
     */
    public boolean explicitLock() {
        return explicitLock;
    }

    /**
     * @return Mini future ID.
     */
    public IgniteUuid miniId() {
        return miniId;
    }

    /**
     * @param miniId Mini future ID.
     */
    public void miniId(IgniteUuid miniId) {
        this.miniId = miniId;
    }

    /**
     * @return Subject ID.
     */
    @Nullable public UUID subjectId() {
        return subjId;
    }

    /**
     * @return Task name hash.
     */
    public int taskNameHash() {
        return taskNameHash;
    }

    /**
     * @return Topology version.
     */
    @Override public long topologyVersion() {
        return topVer;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneCallsConstructors"})
    @Override public GridTcpCommunicationMessageAdapter clone() {
        GridNearTxFinishRequest _clone = new GridNearTxFinishRequest();

        clone0(_clone);

        return _clone;
    }

    /** {@inheritDoc} */
    @Override protected void clone0(GridTcpCommunicationMessageAdapter _msg) {
        super.clone0(_msg);

        GridNearTxFinishRequest _clone = (GridNearTxFinishRequest)_msg;

        _clone.miniId = miniId;
        _clone.explicitLock = explicitLock;
        _clone.topVer = topVer;
        _clone.subjId = subjId;
        _clone.taskNameHash = taskNameHash;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("all")
    @Override public boolean writeTo(ByteBuffer buf) {
        commState.setBuffer(buf);

        if (!super.writeTo(buf))
            return false;

        if (!commState.typeWritten) {
            if (!commState.putByte(null, directType()))
                return false;

            commState.typeWritten = true;
        }

        switch (commState.idx) {
            case 20:
                if (!commState.putBoolean(null, explicitLock))
                    return false;

                commState.idx++;

            case 21:
                if (!commState.putGridUuid(null, miniId))
                    return false;

                commState.idx++;

            case 22:
                if (!commState.putLong(null, topVer))
                    return false;

                commState.idx++;

            case 23:
                if (!commState.putUuid(null, subjId))
                    return false;

                commState.idx++;

            case 24:
                if (!commState.putInt(null, taskNameHash))
                    return false;

                commState.idx++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("all")
    @Override public boolean readFrom(ByteBuffer buf) {
        commState.setBuffer(buf);

        if (!super.readFrom(buf))
            return false;

        switch (commState.idx) {
            case 20:
                if (buf.remaining() < 1)
                    return false;

                explicitLock = commState.getBoolean(null);

                commState.idx++;

            case 21:
                IgniteUuid miniId0 = commState.getGridUuid(null);

                if (miniId0 == GRID_UUID_NOT_READ)
                    return false;

                miniId = miniId0;

                commState.idx++;

            case 22:
                if (buf.remaining() < 8)
                    return false;

                topVer = commState.getLong(null);

                commState.idx++;

            case 23:
                UUID subjId0 = commState.getUuid(null);

                if (subjId0 == UUID_NOT_READ)
                    return false;

                subjId = subjId0;

                commState.idx++;

            case 24:
                if (buf.remaining() < 4)
                    return false;

                taskNameHash = commState.getInt(null);

                commState.idx++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public byte directType() {
        return 52;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return GridToStringBuilder.toString(GridNearTxFinishRequest.class, this, "super", super.toString());
    }
}
