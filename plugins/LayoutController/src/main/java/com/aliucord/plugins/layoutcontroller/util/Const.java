package com.aliucord.plugins.layoutcontroller.util;

public class Const {

    public static final boolean PREFERENCE_DEFAULT_VALUE = false;
    
    public static class Key {
        public static final String CALL_BUTTONS_KEY = "callButtons";
        public static final String GIFT_BUTTON_KEY = "giftButton";
        public static final String INVITE_BUTTON_CHANNELS_KEY = "channelInviteButton";
        public static final String INVITE_BUTTON_MEMBERS_KEY = "memberInviteButton";
        public static final String SEARCH_BOX_KEY = "searchBoxDM";
        public static final String NOTES_KEY = "notes";
        public static final String UNTRUSTED_DOMAINS_KEY = "untrustedDomains";
    }
    
    public static class Description {
        public static final String CALL_BUTTONS_DESCRIPTION = "Remove call buttons from the user sheet";
        public static final String GIFT_BUTTON_DESCRIPTION = "Remove the Nitro Gift button";
        public static final String INVITE_BUTTON_CHANNELS_DESCRIPTION = "Remove the invite button in channels list";
        public static final String INVITE_BUTTON_MEMBERS_DESCRIPTION = "Remove the invite button in members list";
        public static final String SEARCH_BOX_DESCRIPTION = "Remove the search box in DM list";
        public static final String NOTES_DESCRIPTION = "Remove the notes box from the user sheet";
        public static final String UNTRUSTED_DOMAINS_DESCRIPTION = "Remove the untrusted domain dialog";
    }
}