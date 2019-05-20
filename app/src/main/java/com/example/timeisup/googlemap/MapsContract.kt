package com.example.timeisup.googlemap

import com.example.hclee.lifeguard.BasePresenter
import com.example.hclee.lifeguard.BaseView

/**
 * Created by hclee on 2019-05-20.
 */

interface MapsContract {
    interface View: BaseView<Presenter> {

    }

    interface Presenter: BasePresenter {

    }
}