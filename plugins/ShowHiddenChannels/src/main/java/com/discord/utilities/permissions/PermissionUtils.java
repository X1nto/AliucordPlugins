package com.discord.utilities.permissions;

import com.discord.api.channel.Channel;

public final class PermissionUtils {
    public static final PermissionUtils INSTANCE = new PermissionUtils();

    public final boolean hasAccess(Channel channel, Long l) {
        return false;
    }

}

