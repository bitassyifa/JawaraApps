package com.projectassyifa.jawaraapps.config

import com.projectassyifa.jawaraapps.home.layout.HomeLayout
import com.projectassyifa.jawaraapps.login.layout.LoginLayout
import com.projectassyifa.jawaraapps.maps.layout.MapAgentActivity
import com.projectassyifa.jawaraapps.otp.VerifyOtpActivity
import com.projectassyifa.jawaraapps.pickup.data.PickupAdapter
import com.projectassyifa.jawaraapps.pickup.data.PickupStatusAdapter
import com.projectassyifa.jawaraapps.pickup.layout.Pickup
import com.projectassyifa.jawaraapps.pickup.layout.StatusPickupActivity
import com.projectassyifa.jawaraapps.register.layout.RegisterLayout
import com.projectassyifa.jawaraapps.user.layout.EditProfil
import com.projectassyifa.jawaraapps.user.layout.UserProfil
import com.projectassyifa.jawaraapps.wallet.data.HistoryTransAdapter
import com.projectassyifa.jawaraapps.wallet.layout.Income
import com.projectassyifa.jawaraapps.wallet.layout.Spending
import com.projectassyifa.jawaraapps.wallet.layout.Wallet
import com.projectassyifa.jawaraapps.withdraw.data.AdapterListRek
import com.projectassyifa.jawaraapps.withdraw.data.AdapterListbank
import com.projectassyifa.jawaraapps.withdraw.layout.AddBankAccount
import com.projectassyifa.jawaraapps.withdraw.layout.ListRekActivity
import com.projectassyifa.jawaraapps.withdraw.layout.WithdrawActivity
import dagger.Component

@Component(modules = [NetworkModul::class])
interface ApplicationComponent {
    fun inject(loginLayout: LoginLayout)
    fun inject(homeLayout: HomeLayout)
    fun inject(registerLayout: RegisterLayout)
    fun inject(mapAgentActivity: MapAgentActivity)
    fun inject(pickupAdapter: PickupAdapter)
    fun inject(pickup: Pickup)
    fun inject(statusPickupActivity: StatusPickupActivity)
    fun inject(pickupStatusAdapter: PickupStatusAdapter)
    fun inject(userProfil: UserProfil)
    fun inject(editProfil: EditProfil)
    fun inject(verifyOtpActivity: VerifyOtpActivity)
    fun inject(wallet: Wallet)
    fun inject(historyTransAdapter: HistoryTransAdapter)
    fun inject(income: Income)
    fun inject(adapterListbank: AdapterListbank)
    fun inject(listRekActivity: ListRekActivity)
    fun inject(addBankAccount: AddBankAccount)
    fun inject(adapterListRek: AdapterListRek)
    fun inject(withdrawActivity: WithdrawActivity)
    fun inject(spending: Spending)
}