# SalesApp

Android shopping app with Firestore-backed catalog, cart, and checkout. Includes PayPal Checkout integration and sample product seeding for quick setup.

## Features
- Customer browsing with locally-referenced product imagery and Firestore-sourced details.
- Cart management with quantity updates and price totals.
- Checkout flow using PayPal Checkout SDK with per-order account capture.
- Sample product seeding when Firestore is empty.

## Setup
1. Configure Firebase project credentials via `google-services.json`.
2. Ensure Firestore has read/write rules suitable for development or supply proper security rules.
3. Build the app with Gradle. Tests may require network access to download the Gradle distribution.
4. Replace drawable placeholders under `app/src/main/res/drawable/` with final assets if desired.

## PayPal
- The app initializes PayPal Checkout with the provided client ID in `SchoolApp`.
- Checkout UI prompts for the user's PayPal account before initiating payment capture.

## Sample Products
The app seeds Firestore with a starter catalog (if empty) using deterministic document IDs to avoid duplicates.
