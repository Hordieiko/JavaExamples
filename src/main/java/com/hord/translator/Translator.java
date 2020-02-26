package com.hord.translator;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Translator {
    private static Translator ourInstance = new Translator();

    public static synchronized Translator getInstance() {
        return ourInstance;
    }

    private Translator() {
    }

    public String translate(String targetLanguage, String text) {
        return translate("auto", targetLanguage, text);
    }

    public String translate(String sourceLanguage, String targetLanguage, String text) {
        try {
            String urlText = generateURL(sourceLanguage, targetLanguage, text);
            URL url = new URL(urlText);
            String rawData = urlToText(url);
            if (Objects.isNull(rawData) || rawData.isEmpty()) {
                return "";
            } else {
                String[] raw = rawData.split("\"");
                return raw.length < 2 ? "" : capitalize(raw[1]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String capitalize(final String words) {
        return Stream.of(words.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    private String generateURL(String sourceLanguage, String targetLanguage, String text) throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(text, "UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("http://translate.google.com/translate_a/single");
        sb.append("?client=webapp");
        sb.append("&hl=en");
        sb.append("&sl=");
        sb.append(sourceLanguage);
        sb.append("&tl=");
        sb.append(targetLanguage);
        sb.append("&q=");
        sb.append(encoded);
        sb.append("&multires=1");
        sb.append("&otf=0");
        sb.append("&pc=0");
        sb.append("&trs=1");
        sb.append("&ssel=0");
        sb.append("&tsel=0");
        sb.append("&kc=1");
        sb.append("&dt=t");
        sb.append("&ie=UTF-8");
        sb.append("&oe=UTF-8");
        sb.append("&tk=");
        sb.append(generateToken(text));
        return sb.toString();
    }

    private String urlToText(URL url) {
        URLConnection urlConn = null;
        try {
            urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream is = urlConn.getInputStream()) {
            try (Reader r = new InputStreamReader(is, Charset.forName("UTF-8"))) {
                StringBuilder buf = new StringBuilder();
                int data;
                while ((data = r.read()) != -1) {
                    buf.append((char) data);
                }
                return buf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private int[] TKK() {
        return new int[]{406398, 2087938574};
    }

    private int RL(int a, String b) {
        for (int c = 0; c < b.length() - 2; c += 3) {
            int d = b.charAt(c + 2);
            d = d >= 'A' ? d - 87 : d - 48;
            d = b.charAt(c + 1) == '+' ? shr32(a, d) : a << d;
            a = b.charAt(c) == '+' ? a + (d & -1) : a ^ d;
        }

        return a;
    }

    private int shr32(int x, int bits) {
        if (x < 0) {
            long x_l = 4294967295L + (long) x + 1L;
            return (int) (x_l >> bits);
        } else {
            return x >> bits;
        }
    }

    private String generateToken(String text) {
        int[] tkk = TKK();
        int b = tkk[0];
        int e = 0;
        int f = 0;

        ArrayList d;
        int g;
        for (d = new ArrayList(); f < text.length(); ++f) {
            g = text.charAt(f);
            if (128 > g) {
                d.add(e++, g);
            } else {
                if (2048 > g) {
                    d.add(e++, g >> 6 | 192);
                } else if (55296 == (g & 'ﰀ') && f + 1 < text.length() && 56320 == (text.charAt(f + 1) & 'ﰀ')) {
                    int var10000 = 65536 + ((g & 1023) << 10);
                    ++f;
                    g = var10000 + (text.charAt(f) & 1023);
                    d.add(e++, g >> 18 | 240);
                    d.add(e++, g >> 12 & 63 | 128);
                } else {
                    d.add(e++, g >> 12 | 224);
                    d.add(e++, g >> 6 & 63 | 128);
                }

                d.add(e++, g & 63 | 128);
            }
        }

        g = b;

        for (e = 0; e < d.size(); ++e) {
            g += (Integer) d.get(e);
            g = RL(g, "+-a^+6");
        }

        g = RL(g, "+-3^+b+-f");
        g ^= tkk[1];
        long a_l;
        if (0 > g) {
            a_l = 2147483648L + (long) (g & 2147483647);
        } else {
            a_l = (long) g;
        }

        a_l = (long) ((double) a_l % Math.pow(10.0D, 6.0D));
        return String.format(Locale.US, "%d.%d", a_l, a_l ^ (long) b);
    }
}
