package com.ood.clean.waterball.a1a2bsdk.core;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

public interface GameModuleFactory {
    GameModule createModule(ModuleName moduleName);
}
