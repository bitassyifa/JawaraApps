package com.projectassyifa.jawaraapps.config

import com.projectassyifa.jawaraapps.login.data.LoginAPI
import com.projectassyifa.jawaraapps.maps.data.AgentAPI
import com.projectassyifa.jawaraapps.pickup.data.PickupAPI
import com.projectassyifa.jawaraapps.register.data.RegisterAPI
import com.projectassyifa.jawaraapps.user.data.UserAPI
import com.projectassyifa.jawaraapps.wallet.data.WalletAPI
import com.projectassyifa.jawaraapps.withdraw.data.WithdrawAPI
import dagger.Module
import dagger.Provides


@Module
class NetworkModul {
    @Provides
    fun provideLoginAPI(): LoginAPI {
        return Connection.urlCon().create(LoginAPI::class.java)
    }
    @Provides
    fun provideUserAPI(): UserAPI {
        return Connection.urlCon().create(UserAPI::class.java)
    }
    @Provides
    fun provideRegisterAPI(): RegisterAPI {
        return Connection.urlCon().create(RegisterAPI::class.java)
    }
    @Provides
    fun provideAgentAPI(): AgentAPI {
        return Connection.urlCon().create(AgentAPI::class.java)
    }
    @Provides
    fun providePickupAPI(): PickupAPI {
        return Connection.urlCon().create(PickupAPI::class.java)
    }
    @Provides
    fun provideWalletAPI(): WalletAPI {
        return Connection.urlCon().create(WalletAPI::class.java)
    }
    @Provides
    fun provideWithdrawAPI(): WithdrawAPI {
        return Connection.urlCon().create(WithdrawAPI::class.java)
    }
}