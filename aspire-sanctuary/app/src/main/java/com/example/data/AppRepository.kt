package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppRepository(private val appDao: AppDao) {

    val allProperties: Flow<List<Property>> = appDao.getAllProperties()
    val favoriteProperties: Flow<List<Property>> = appDao.getFavoriteProperties()
    val allBookings: Flow<List<Booking>> = appDao.getAllBookings()

    fun getPropertyById(id: Int): Flow<Property?> = appDao.getPropertyById(id)

    suspend fun toggleFavorite(propertyId: Int) {
        val property = appDao.getPropertyById(propertyId).first()
        if (property != null) {
            appDao.updateFavoriteStatus(propertyId, !property.isFavorite)
        }
    }

    suspend fun setFavorite(propertyId: Int, isFav: Boolean) {
        appDao.updateFavoriteStatus(propertyId, isFav)
    }

    suspend fun bookProperty(booking: Booking) {
        appDao.insertBooking(booking)
    }

    suspend fun cancelBooking(booking: Booking) {
        appDao.deleteBooking(booking)
    }

    suspend fun checkAndPrepopulate() {
        val existing = appDao.getAllProperties().first()
        if (existing.isEmpty()) {
            val initialList = listOf(
                Property(
                    id = 1,
                    name = "Villa Monolith",
                    location = "Amalfi Coast, Italy",
                    category = "Villas",
                    pricePerNight = 4200.0,
                    description = "Designed by Pritzker-winning architects, this sanctuary is perched on the highest ridge of the Amalfi Coast. Every angle was calculated to capture the changing light of the Mediterranean. The interiors feature hand-honed basalt floors, Venetian plaster walls, and custom-designed furniture that evokes a sense of timeless modernism. Whether hosting a private gala on the tiered terraces or finding solitude in the subterranean spa, the estate offers a level of seclusion rarely found in the modern world.",
                    beds = 6,
                    baths = 8,
                    areaSqM = 1200,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuC0xZmcalHAuL2yraidCV9nCf5Locssje8wg7arGMLy4oGqb723LH-4OhyB-Rq1nE9m5YoT0YXJj9zbPbbijQFIqGdA7gNQKllgHrX8BfS_y0JzuXKGeMJrguYPGi5cz9HIvkBcWD9v5Kkc-STKb_9SAJc0aVR1pDTzTrJIoCmf0KZ7YZjevurE5nEV6o2Ffz4-sO-GUH-cv2BSP-giU0_50ZRUZ5fWYN0Pz64YZIYdZGm5OkFhQrnDG_MTImV_lDQsxHorVzUBTWbS,https://lh3.googleusercontent.com/aida-public/AB6AXuCAQ_ygEjGzq-P_SROqLD_lG7UhLpjVaeLpnntMxisEcoMmGdEOlUpAKC6GQG_HwNYszR8FA1K37rvGEjOAZM191iZV8n7ePYz-TH5nDXfi4EiQSN30rawYab9k1SZIxZVtuY9zFTShbxRYHeSijLvLrN4wn4Xs38YhH0x1Fj9paXYRGBbs7XCiNV2McnD0SR4J-QSDPDhA5G25vTgqVLdqzSTDiAhRCJzs6dxlNvv21Jdh3vIqwJbtFuO8Y4LyV8eQrbudIUtdpndM,https://lh3.googleusercontent.com/aida-public/AB6AXuBUWVDcbh8s0QX6ot00rCdcfnEFiOFz5uZqIGwe9fp9dBbrBucc89YGD010rXgiVwab8ZwA-Zz9Hdf15wzugeELNqLvfZ3e9-UnLX6Z655zZrwJKKpQBEfcH2e4VAm8jqKCQPZo9NFBnbXMCq-mtui_7646h6x6oUdxHcX1hHvKgynsYkgMLfgnKdzxt-k9foaQFPhoSvEEETFZA5pvJwPiTHK2hRpDqYay-heW5uJCx8bSgmv2BN--lyY0uQtJBNYXix6RsbJqjEpV",
                    isFavorite = true,
                    amenitiesCommaSeparated = "24/7 Personal Butler, Private Michelin Chef, Secluded Beach Access, Infinity Pool, Wine Cellar, Helipad, Cinema Room, Spa & Wellness"
                ),
                Property(
                    id = 2,
                    name = "The Obsidian Chalet",
                    location = "Zermatt, Switzerland",
                    category = "Ski Chalets",
                    pricePerNight = 2800.0,
                    description = "Nestled high up in the Swiss Alps, The Obsidian Chalet is an architectural masterpiece of dark charred wood, basalt stone, and double-height glass walls. Offering uninterrupted views of the Matterhorn, this alpine sanctuary provides the ultimate winter getaway. Features an indoor heated basalt-lined pool, a sunken fire pit, and bespoke wood craftsmanship throughout.",
                    beds = 4,
                    baths = 5,
                    areaSqM = 450,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuCVrTxQkF8sIRr5irJeu83mnMCtCsj_1iKxRAdz0J-RvK-eC7szjG542qZc90lT6TrwuAuAJBpBZ6hDaEav3t3SfmQY_krPYZiA4Qvtw8aY-QyWr_I_jdNNmTl-ModQpH0vZrZeKC-PMGmTsc9sVTS-fPbrymZxoioO1C_oQo7bSS1OA9JoEz8q_3Q9kv8j88CZxzfHeDmd05gTCNCSUNAuRq3cX9rsWUzZoOtxFHhCgv-MYEeL1fV53DlWUUfQ5deQYin07QKWlzD2,https://lh3.googleusercontent.com/aida-public/AB6AXuBCf4J8RsO91q2mLlOt6J5mKBvexYU6PbdZtne3fC5pGchCvA-zugEnaK1T-Yc0Dm5fE4hQuko1S8SeH9j8zBFGnSURXhFXCVy3LMCCYx9sdAiypi16dozecUDV3CujIRKuoEFp3uZ0cUZn6D16IyX0fcDegcCjT65TA7VbiTSVApP3ay8pS0GMobliqDngTOUcxcPs48waqVKFAhhwZ2eaC_T-XoTlEh7Ug93NB6fKOmBDSpXypdj29V_bRp1lvmNH9jkgRP_iunfQ",
                    isFavorite = false,
                    amenitiesCommaSeparated = "Private Ski Guide, Basalt Pool, Fireplace, Wine Cellar, Wellness Sauna"
                ),
                Property(
                    id = 3,
                    name = "Amalfi Coastal Retreat",
                    location = "Amalfi Coast, Italy",
                    category = "Villas",
                    pricePerNight = 3500.0,
                    description = "Set dramatically on the cliffs of the Amalfi Coast, this historic estate blends traditional Mediterranean architecture with ultra-luxury modern comfort. Surrounded by lush lemon groves and cascading pink bougainvillea, the expansive terrace features custom luxury loungers and direct lift access down to a private cove.",
                    beds = 5,
                    baths = 6,
                    areaSqM = 650,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuAKZHOtP36bHCT1Kz-ZRk-qw-lLNSttHcjabJKwPnnMOJgdFvJMIyKoD6v-HwElfsPe9yns-_WWfd_ZCsFnVkoGwe6GGlLDuLT7yq_TGFfAfVQYj9r3xFnB-FQxjGNSJeU5Lxhip_JKLYJsJB7DnE4z42U_ezJUYXJrAU3OWBHpvyjzsRL6JMmE3mg0uT7YPOB8Iq_XwGhOM8wnkCqsuC0aoZHNVH9t_VS6crVkCKZhXj3_9Gn3QgP0dhkno6KJBg97RCGBaaYsiduF",
                    isFavorite = true,
                    amenitiesCommaSeparated = "Terrace Loungers, Private Boat Access, Lemon Grove Garden, Outdoor Infinity Pool"
                ),
                Property(
                    id = 4,
                    name = "High-Desert Monolith",
                    location = "Joshua Tree, California",
                    category = "Estates",
                    pricePerNight = 1950.0,
                    description = "A striking Brutalist-style sanctuary integrated seamlessly into the ancient rock formations of Joshua Tree. Featuring sharp geometric concrete structures that contrast beautifully with the ethereal desert sunsets, this off-grid estate has been featured in top design publications worldwide.",
                    beds = 3,
                    baths = 3,
                    areaSqM = 350,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuD5gjAU2dGWs3nF33cmARBwZk8cKFZpPuGZxQ9qLFybiZLAQ7_l-9q4uqUkqhboKoM5b2nCBWlLyjKIFbP4UDH0iO-xzvfFZvoFPkeKsJGGxIlbFUsaKCh1w5yk-OhLDsA8YTsXl7cthGP-31rWYg8neoSPCf9Wyh-N8kCsuU5ulHs4EFKY8bcoargFbf2uQqgADRol5W9FSk45p9pAXL7HpxJh59g68rraocGqlEzKzCjO7H_pD9DDbogpcknfvL4cr65yKtWq4nhI",
                    isFavorite = false,
                    amenitiesCommaSeparated = "Desert Fire Pit, Mirrored Plunge Pool, Star Observatory, High-End Sound System"
                ),
                Property(
                    id = 5,
                    name = "The Hidden Ryokan",
                    location = "Kyoto, Japan",
                    category = "Estates",
                    pricePerNight = 2200.0,
                    description = "A traditional yet fully modernized ryokan located in a secluded forest of Kyoto. Featuring charred wood architecture (shou sugi ban), sliding shoji screens, a private rock garden, and a hinoki cypress wood onsen. The perfect environment for absolute tranquility and wabi-sabi mindfulness.",
                    beds = 4,
                    baths = 4,
                    areaSqM = 400,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuDqWNosowvuEt7gJfm5lHkcfeJ6lGpnzrzLakGEqPqGYjAdfDsvwT7iMGIst3pFFY5iyNZGV_Z6QUftxgOeZeMfQfKoc6GdrSzenjcd-CgLFA-MZYTTrVUNlg4LtTAS_VQUv_VCF6SshpTEWYdHAD8t_Q1s8Shf_qGHoAtPwHAhp5mZfF2K-dRzjOcKLwDKu041j_Bbz2BYXS65HmGGOSkJy6QXs3vSlZGuGcz65aHNEwO-zaqN3-YotV8Tqo6ogNsdJg2HvorMqptz",
                    isFavorite = true,
                    amenitiesCommaSeparated = "Hinoki Onsen, Private Rock Garden, Tea Ceremony Lounge, 24/7 Zen Host"
                ),
                Property(
                    id = 6,
                    name = "Zen Sanctuary",
                    location = "Kyoto, Japan",
                    category = "Estates",
                    pricePerNight = 2400.0,
                    description = "A modernist glass pavilion nestled in a bamboo forest in Kyoto, Japan. The architecture is sharp and linear with black steel frames. The interior is minimally furnished with tatami mats and a low-profile mahogany table. Soft morning mist drifts through the trees outside, offering a mesmerizing atmosphere.",
                    beds = 3,
                    baths = 3,
                    areaSqM = 320,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuA4p9EQ8DaZqWWFTA2TUTjh18KcIjtuppZgj-rgFnFCEQKkDl5TacyCCb7q81Ypj6CCBvT7zSmshHh5WA7ZbOPt_F8_4BxgaZfhg5BDQ_OW-KJ5DvGRAlqafmDAMA_djpiiu_Vp9Mq3Eu29uzQ_IgLKmkQ2QJn9YrxCsm4Y3c8htcBFy1Lmb3Px6YRLeKJ5BzoBdF0MWw-7EnWV0ikoqOIWv4H-HD5ZRnM8BdQtGQYdLbkPrIrV9i4Qxf9YKLBgjQG6s6yOV-9rzhGr",
                    isFavorite = false,
                    amenitiesCommaSeparated = "Glass Walls, Zen Bamboo Walkway, Private Spa Room, Organic Culinary Service"
                ),
                Property(
                    id = 7,
                    name = "Dune Observance",
                    location = "Sossusvlei, Namibia",
                    category = "Estates",
                    pricePerNight = 3900.0,
                    description = "A luxury desert compound in Namibia, featuring reddish-orange sand dunes in the background and a sleek, circular stone structure with an open-air fire pit. The sky is dark and filled with a brilliant, clear view of the Milky Way galaxy. The aesthetic is raw, primeval, yet deeply sophisticated.",
                    beds = 4,
                    baths = 4,
                    areaSqM = 550,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuCeiSOqZbhg2iSRdLBvs56fHcmqV6vnfLvdO_xr0iJnxCNrkQLxJ5qh-5AndDz70zVpk6U3kAA4frkC__LxANsDeiIQ-fxZnRmysDl7JRW8x7YA3TReFXRRIkw4GBJGB8Lc4omhx1CXB8V8vDuzUx3XI2V44v1z13DZWN7hAM1UTjdt5xRs15QiY8zkNmebX3EzFaNQrl3LZdg5YJI0vg54kdLCJKv3CbVBYSTFxuPWK_-Rhp3Apy89Ufg63pEWWgDRu_Fn6nadGgTT",
                    isFavorite = false,
                    amenitiesCommaSeparated = "Stargazing Telescope, Open-Air Stone Firepit, Infinity Pool, 4x4 Luxury Safari Guide"
                ),
                Property(
                    id = 8,
                    name = "Azure Horizon Villa",
                    location = "Amalfi, Italy",
                    category = "Villas",
                    pricePerNight = 4800.0,
                    description = "A breathtaking infinity pool overlooking the Mediterranean coast in Amalfi, Italy. The pool's edge blends seamlessly with the azure sea. Dramatic limestone cliffs surround the property. The design features gorgeous white marble surfaces, state-of-the-art automated features, and an elite personal chef.",
                    beds = 6,
                    baths = 7,
                    areaSqM = 850,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuCqa_8i9SqdQ_pYw3mWzMMA8GEvJahXhaRugI8IH29bU5b8MKACg0yVWY1Aqjk4gKIptFQX3iobVWB-XOC1CWemGmBnLC1IhBaGUO9dKxRSuXojLd3iYYssErzMAvXj1VWGDpq8exME2uW4jtP5y4AYxpC8fDy0qEdKDwhgV-VkgRNNkB63RRjHN-ixiCrNR3dGok4HOraJ7Z6whaKTaCs9wEZ7yIZr6nwHiQXtd0_HPYpkoCT31VVdmMf2GzSOzjD-BgKDiKdr94e8",
                    isFavorite = true,
                    amenitiesCommaSeparated = "Infinity Pool, White Marble Lounge, Michelin Star Chef, Heli-pad access"
                ),
                Property(
                    id = 9,
                    name = "The Bamboo Sanctuary",
                    location = "Ubud, Bali",
                    category = "Villas",
                    pricePerNight = 3100.0,
                    description = "A lush tropical estate in Bali with open-air bamboo architecture and a private tiered waterfall pool. Dense emerald green jungle surroundings with soft morning mist. The lighting is diffused and natural, highlighting the organic textures of wood and stone. Luxury wellness retreat aesthetic with deep green and warm wood palette.",
                    beds = 4,
                    baths = 5,
                    areaSqM = 600,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOcaKmLqWvdjD11VK6E3B52suS5g6uTvMgLxFKhTxW6Tw3EtsBDptzawV44oIibE6F5DN8Ke3jcO6MPxPF7s_EFkvSX0gBpiSJFvBwNmTZsYt23SCLyXtUoCPT2fWPXRyizZ5roaiaGTckuBdEwvmmBjPK7t26QWGJ_kt7W45O3jAPN_fmYPzYq5Af4lu3ZX1U4GmxDyLXCI_wkvrLNQoh6xSPPLd6Ao5-QF8yndvkz3t7A0JZI7gw-tocDAvFlAwOUxfYuF09BQ93",
                    isFavorite = false,
                    amenitiesCommaSeparated = "Waterfall Pool, Private Yoga Shala, Full In-House Spa, Jungle Trek Guide"
                ),
                Property(
                    id = 10,
                    name = "Kythnos Hideaway",
                    location = "Kythnos, Greece",
                    category = "Islands",
                    pricePerNight = 5500.0,
                    description = "A sleek white modernist villa on a private Greek island overlooking the Aegean Sea. Perfect minimalist geometry, stark white walls against an impossible deep blue ocean. Bright, airy high-key lighting, capturing the essence of Mediterranean luxury. Minimalist aesthetic with focus on vast space and light.",
                    beds = 5,
                    baths = 6,
                    areaSqM = 550,
                    imageUrlsCommaSeparated = "https://lh3.googleusercontent.com/aida-public/AB6AXuBi4qyIS6n3mfppKKNrCXVYDJL5epCJ7JJ581cGKpwz6mVIN3Olp9WMyWRBlL3o7OSmnV3AMbZEAHrZ0yCN8BIwasnDhu4KDhrj36v2dSYXV4ExiOPTHYtNleZvgFlvZK1bW7wcmW_Lyrq7FOdFGvrlc0behEOO7x2Gxglo7GS36oMsjFeG7v_-2LHPlxYvLfy7LieQs7G6XuNYe7ydGOCNUhyyQdPlSzmHryAqxI178aj4h2JgEtE2t306x95BHxgkCO4tl_2re1sR",
                    isFavorite = false,
                    amenitiesCommaSeparated = "Aegean View Lounge, Private Yacht Dock, Heli-pad, Saltwater Infinity Pool"
                )
            )
            appDao.insertProperties(initialList)
        }

        // Prepopulate a sample mock upcoming stay if none exists so Julian's profile has content
        val currentBookings = appDao.getAllBookings().first()
        if (currentBookings.isEmpty()) {
            appDao.insertBooking(
                Booking(
                    id = 1,
                    propertyId = 2,
                    propertyName = "The Obsidian Chalet",
                    propertyLocation = "Zermatt, Switzerland",
                    propertyImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCVrTxQkF8sIRr5irJeu83mnMCtCsj_1iKxRAdz0J-RvK-eC7szjG542qZc90lT6TrwuAuAJBpBZ6hDaEav3t3SfmQY_krPYZiA4Qvtw8aY-QyWr_I_jdNNmTl-ModQpH0vZrZeKC-PMGmTsc9sVTS-fPbrymZxoioO1C_oQo7bSS1OA9JoEz8q_3Q9kv8j88CZxzfHeDmd05gTCNCSUNAuRq3cX9rsWUzZoOtxFHhCgv-MYEeL1fV53DlWUUfQ5deQYin07QKWlzD2",
                    checkInDate = "Dec 14, 2024",
                    checkOutDate = "Dec 21, 2024",
                    totalPrice = 19600.0,
                    guestsCount = 2
                )
            )
        }
    }
}
