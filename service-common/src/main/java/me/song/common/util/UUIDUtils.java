package me.song.common.util;

import java.util.UUID;

/**
 * @author Songwe
 * @since 2023/3/30 14:25
 */
public class UUIDUtils {

    public static String getUUID() {
        final UUID uuid = UUID.randomUUID();
        return Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
    }
}
