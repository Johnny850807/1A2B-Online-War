package com.ood.clean.waterball.a1a2bsdk.core.modules.games;

import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;

import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.RequestStatus;

import static container.Constants.Events.Games.ENTERGAME;

public abstract class AbstractOnlineGameModule extends AbstractGameModule implements OnlineGameModule{

    @Override
    public void enterGame() {
        Protocol protocol = protocolFactory.createProtocol(ENTERGAME, RequestStatus.request.toString(),
                gson.toJson(new PlayerRoomIdModel(getCurrentPlayer().getId(),
                        getCurrentGameRoom().getId())));
        client.broadcast(protocol);
    }

    protected abstract Player getCurrentPlayer();
    protected abstract GameRoom getCurrentGameRoom();

}
