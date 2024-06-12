package com.miirrr.qrscan.services.keyboardlayout;

import java.util.Map;

public interface KeyboardLayoutService {

    boolean isEngUs();

    Map<Integer, String> getCurrentKeyboardLayout();
}
