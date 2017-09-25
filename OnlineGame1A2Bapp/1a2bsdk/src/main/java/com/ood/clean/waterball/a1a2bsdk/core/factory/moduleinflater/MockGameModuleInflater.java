package com.ood.clean.waterball.a1a2bsdk.core.factory.moduleinflater;


import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.mock.MockUserSigningModule;

import java.util.Map;

public class MockGameModuleInflater implements GameModuleInflater{

    @Override
    public void onPrepareModules(Map<ModuleName, GameModule> moduleMap) {
        moduleMap.put(ModuleName.SIGNING, new MockUserSigningModule());
    }

}
