package com.ood.clean.waterball.a1a2bsdk.core;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModuleImp;

import java.util.Map;



public class ReleaseGameModuleInflater implements GameModuleInflater {

    @Override
    public void onPrepareModules(Map<ModuleName, GameModule> moduleMap) {
        moduleMap.put(ModuleName.SIGNING, new UserSigningModuleImp());
    }

}
