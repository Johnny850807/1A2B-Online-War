package com.ood.clean.waterball.a1a2bsdk.core.factory;

import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.signIn.UserSigningModuleImp;

import java.util.Map;



public class ReleaseGameModuleInflater implements GameModuleInflater {

    @Override
    public void onPrepare(Map<ModuleName, GameModule> moduleMap) {
        moduleMap.put(ModuleName.SIGNING, new UserSigningModuleImp());
    }

}
