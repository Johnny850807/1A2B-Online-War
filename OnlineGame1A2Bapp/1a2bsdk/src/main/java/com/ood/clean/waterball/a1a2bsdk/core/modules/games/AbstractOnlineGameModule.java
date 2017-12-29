package com.ood.clean.waterball.a1a2bsdk.core.modules.games;


import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import container.protocol.Protocol;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.RequestStatus;

import static container.Constants.Events.Games.ENTERGAME;

public abstract class AbstractOnlineGameModule extends AbstractGameModule implements OnlineGameModule{
    protected UserSigningModule signingModule;
    protected RoomListModule roomListModule;

    public AbstractOnlineGameModule() {
        signingModule = (UserSigningModule) CoreGameServer.getInstance().getModule(ModuleName.SIGNING);
        roomListModule = (RoomListModule) CoreGameServer.getInstance().getModule(ModuleName.ROOMLIST);
    }

    @Override
    public void enterGame() {
        Protocol protocol = protocolFactory.createProtocol(ENTERGAME, RequestStatus.request.toString(),
                gson.toJson(new PlayerRoomIdModel(signingModule.getCurrentPlayer().getId(),
                        roomListModule.getCurrentGameRoom().getId())));
        client.broadcast(protocol);
    }
}
