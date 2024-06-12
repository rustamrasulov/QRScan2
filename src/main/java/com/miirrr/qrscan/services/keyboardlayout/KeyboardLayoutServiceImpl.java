package com.miirrr.qrscan.services.keyboardlayout;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HKL;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.W32APIOptions;
import java.util.HashMap;
import java.util.Map;

public class KeyboardLayoutServiceImpl implements
    KeyboardLayoutService {

    @Override
    public boolean isEngUs() {
        return getCurrentKeyboardLayout().containsKey(0x0409);
    }

    @Override
    public Map<Integer, String> getCurrentKeyboardLayout() {
        int layoutCode = 0;
        String layoutName;

        User32 user32 = User32.INSTANCE;
        HWND hWnd = user32.GetForegroundWindow();
        if (hWnd != null) {
            int threadId = User32.INSTANCE.GetWindowThreadProcessId(hWnd, (int[]) null);
            HKL keyboardLayoutHKL = User32.INSTANCE.GetKeyboardLayout(threadId);
            if (keyboardLayoutHKL != null) {
                layoutCode = keyboardLayoutHKL.getLanguageIdentifier() & 0xFFFF;

            }
        }

        switch (layoutCode) {
            case 0x0409:
                layoutName = "English (United States)";
            case 0x0419:
                layoutName = "Russian";
            default:
                layoutName = "Unknown";
        }

        Map<Integer, String> result = new HashMap<>();
        result.put(layoutCode, layoutName);

        return result;
    }

    interface User32 extends com.sun.jna.platform.win32.User32 {

        User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

        HKL GetKeyboardLayout(int idThread);

        HWND GetForegroundWindow();

        int GetWindowThreadProcessId(HWND hWnd, int[] lpdwProcessId);
    }
}
