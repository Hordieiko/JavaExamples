package com.hord.crypt;

public class MainCrypt {

    public static void main(String[] args) {
        String encoded = "7G1g14oV81QQx3fpVuiE3BhIgW/gP5bIOAnDJGoDfJ8=";
        String text = "JUPWDCYJSQCWKBOHYQW4BVWT2BC7YRK4AWRDP34FLG2EYFXXHJLSCY4GUWEXUP2AQOW7ZO4N7HSQFHAU75GNYQVXYPQTSMD2RFXC3DV7K7UW7RO3J5UP3R5UTUOXVYVJVR5WT2KSOVWVHIY3AFNYPIWHZN5W7FXZVJN6V4JIZMWFJDPLSQRCE4JR6PNG4F3WFN64VAMOVTL7IXACGKBSN3RBCGCIPA5OEQXZVZRXESEDC4T5AOHC2664FW5WCKRXZHGKKUJITDFHIJH55WBT66BQ3B3HDWGZLEAIYQY";
        System.out.println(encode(text));
    }

    public static String encode(String text) {
        return WBase64.encode(text);
    }



    public static String decrypt(String encoded) {
        return WBase64.decode(encoded);
    }
}
