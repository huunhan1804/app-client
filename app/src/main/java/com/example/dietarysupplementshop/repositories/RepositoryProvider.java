package com.example.dietarysupplementshop.repositories;

public class RepositoryProvider {

    private static final boolean USE_FAKE_REPOSITORIES = false;

    public static ISellerRepository getSellerRepository() {
        if (USE_FAKE_REPOSITORIES) {
            return null;
        } else {
            return SellerRegistrationRepository.getInstance();
        }
    }
}