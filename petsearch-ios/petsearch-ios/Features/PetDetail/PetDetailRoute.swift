//
//  PetDetailRoute.swift
//  ios
//
//  Created by Mohammed Sané on 03/09/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Shared
import SwiftUI

struct PetDetailRoute: NavRoute {

    typealias T = PetDetailViewModel
    
    var route: String {
        return NavigationScreen.PetDetailNavScreen.shared.route
    }
    
    var content: some View {
        return PetDetailScreen(viewModel: viewModel)
    }
    
    var viewModel: PetDetailViewModel {
        @LazyKoin var delegate: PetDetailViewModel
        return delegate
    }
    
    func getArguments() -> Array<String> {
        return [NavigationScreenKt.ARG_PET_INFO]
    }
}
