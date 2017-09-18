package com.ood.clean.waterball.a1a2bsdk.core.factory;


import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import java.util.Map;

public interface GameModuleInflater {
    void onPrepare(Map<ModuleName,GameModule> moduleMap);
}
