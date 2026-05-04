package com.miirrr.qrscan.services.keyboardlayout;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HKL;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.W32APIOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class KeyboardLayoutServiceImpl implements
    KeyboardLayoutService {

    private static final int ENGLISH_US_LAYOUT_CODE = 0x0409;
    private static final int CYRILLIC_LAYOUT_CODE = 0x0419;

    private static final boolean WINDOWS = System.getProperty("os.name", "")
        .toLowerCase(Locale.ENGLISH)
        .contains("win");

    @Override
    public boolean isEngUs() {
        if (!WINDOWS) {
            return true;
        }

        return getCurrentKeyboardLayout().containsKey(ENGLISH_US_LAYOUT_CODE);
    }

    @Override
    public Map<Integer, String> getCurrentKeyboardLayout() {
        int layoutCode = 0;
        String layoutName;

        if (WINDOWS) {
            try {
                User32 user32 = User32Holder.INSTANCE;
                HWND hWnd = user32.GetForegroundWindow();
                if (hWnd != null) {
                    int threadId = user32.GetWindowThreadProcessId(hWnd, (int[]) null);
                    HKL keyboardLayoutHKL = user32.GetKeyboardLayout(threadId);
                    if (keyboardLayoutHKL != null) {
                        layoutCode = keyboardLayoutHKL.getLanguageIdentifier() & 0xFFFF;
                    }
                }
            } catch (Throwable ignored) {
                layoutCode = ENGLISH_US_LAYOUT_CODE;
            }
        } else {
            layoutCode = ENGLISH_US_LAYOUT_CODE;
        }

        switch (layoutCode) {
            case ENGLISH_US_LAYOUT_CODE:
                layoutName = "English (United States)";
                break;
            case CYRILLIC_LAYOUT_CODE:
                layoutName = "Russian";
                break;
            default:
                layoutName = "Unknown";
                break;
        }

        Map<Integer, String> result = new HashMap<>();
        result.put(layoutCode, layoutName);

        return result;
    }

    interface User32 extends com.sun.jna.platform.win32.User32 {

        HKL GetKeyboardLayout(int idThread);

        HWND GetForegroundWindow();

        int GetWindowThreadProcessId(HWND hWnd, int[] lpdwProcessId);
    }

    private static final class User32Holder {
        private static final User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

        private User32Holder() {
        }
    }
}
