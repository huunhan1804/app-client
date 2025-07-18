package com.example.dietarysupplementshop.repositories;

public class RepositoryProvider {

    private static final boolean USE_FAKE_REPOSITORIES = true;

    public static ISellerRepository getSellerRepository() {
        if (USE_FAKE_REPOSITORIES) {
            return FakeSellerRepository.getInstance();
        } else {
            return SellerRegistrationRepository.getInstance();
        }
    }
}