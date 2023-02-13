//
//  HomeScreen.swift
//  ios
//
//  Created by Mohammed Sané on 29/08/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Shared
import SDWebImageSwiftUI

struct HomeScreen: View {
    @Environment(\.colorScheme) var colorScheme
            
    @StateObject var observable = HomeViewModelObservable()
    
    @State private var paginationState = PaginationState.loading
    
    @State private var selectedTab: Int = 0
    
    var body: some View {
        VStack(alignment: .leading, spacing: .zero) {
            locationButton
            
            tabRow
            
            Divider()

            gridView
            
            Spacer()
        }
        .navigationTitle("")
        .navigationBarHidden(true)
        .edgesIgnoringSafeArea(.bottom)
        .background(Color.surface(colorScheme).ignoresSafeArea())
    }
}

extension HomeScreen {
    
    private var locationButton: some View {
        Button(action: {}) {
            VStack(alignment: .leading, spacing: .zero) {
                HStack(alignment: .center, spacing: .zero) {
                    SharedSvgImage("near_me", renderingMode: .template)
                        .frame(width: 24, height: 24)
                    
                    Text("New York City")
                        .style(.titleMedium)
                        .padding(.init(top: .zero, leading: 6, bottom: .zero, trailing:.zero))
                    
                    SharedSvgImage("arrow_drop_down", renderingMode: .template)
                        .frame(width: 24, height: 24)
                }
                Text("20 W 34th St., New York, United States")
                    .style(.bodySmall)
                    .padding(.init(top: 4, leading: 4, bottom: .zero, trailing: .zero))
            }
            .padding(.init(top: 4, leading: 20, bottom: 18, trailing: 24))
        }
    }
    
    private var tabRow: some View {
        TabRow(
            tabs: observable.petTypes.compactMap({ $0.name }),
            selectedTab: $selectedTab.onChange { index in
                // First check if the index is same as already selectedTab or not
                if observable.pagingData.first?.type == observable.petTypes[selectedTab].name {
                    return
                }
                                            
                paginationState = .loading

                // remove all items from the LazyGrid
                observable.pagingData.removeAll()
                
                observable.dispatch(
                    action: OnPetTypeTabChanged(tabName: observable.petTypes[index].name)
                )
            }
        )
    }
    
    private var gridView: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVGrid(
                columns: [GridItem(.flexible(), spacing: 2), GridItem(.flexible(), spacing: 2)],
                spacing: 2
            ) {
                ForEach(observable.pagingData, id: \.id) { petInfo in
                    PetInfoView(petInfo: petInfo) {
                        observable.dispatch(action: NavigateToPetDetail(petInfo: petInfo))
                    }
                    .onAppear {
                        // Very basic and definitely not production ready implementation of pagination
                        // Proper implementation and refinment is required.
                        // Will implement it soon as I keep learning SwiftUI
                        let data = observable.pagingData

                        let thresholdIndex = data.index(data.endIndex, offsetBy: -5)
                        
                        if data.firstIndex(where: { $0.id == petInfo.id }) == thresholdIndex &&
                            paginationState != .loading {
                            paginationState = .loading
                            observable.dispatch(action: LoadPetListNextPage())
                        }
                    }
                }
            }
            
            ProgressView()
                .padding(.init(top: 32, leading: .zero, bottom: 64, trailing: .zero))
                .opacity(paginationState == .loading ? 1 : 0)
        }
        .background(Color.background(colorScheme))
    }
}

struct HomeScreen_Previews: PreviewProvider {
    static var previews: some View {
        HomeScreen()
    }
}
