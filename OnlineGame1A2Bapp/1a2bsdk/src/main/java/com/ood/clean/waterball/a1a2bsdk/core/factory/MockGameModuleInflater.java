package com.ood.clean.waterball.a1a2bsdk.core.factory;


import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.mock.MockUserSigningModule;

import java.util.Map;

public class MockGameModuleInflater implements GameModuleInflater{

    @Override
    public void onPrepare(Map<ModuleName, GameModule> moduleMap) {
        moduleMap.put(ModuleName.SIGNING, new MockUserSigningModule());
    }

}
