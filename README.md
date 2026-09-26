🧵 AI-Powered Virtual Business Manager for Artisans
Bridging India's handicraft artisans to digital commerce — one photo at a time

Smart India Hackathon 2026 · Problem Statement ID: SIH26090 Team: Ukdi_cha_modak

Flutter FastAPI Groq OpenCV Bhashini Supabase PostgreSQL License

</div>
📖 Table of Contents
Problem Statement
Our Solution
Key Features
Tech Stack
System Architecture
User Flow
Screenshots
Data & Research Sources
Team
Roadmap
License
🎯 Problem Statement

India is home to an estimated 64.66 lakh artisans — over 70% women, nearly 89% rural — who depend heavily on periodic physical exhibitions (Shilp Samagam, Dilli Haat, Surajkund Mela) for sales, but lack continuous, year-round access to digital markets.

Barrier	Data
Rural internet access	24% (vs. 66% urban) — NSSO, 2024
Artisans with digital-selling training	~20% — DEF survey, n=2,000+, via IDR 2025
Rural computer ownership	4.4% (vs. 23.4% urban) — NSSO 75th Round, 2017-18
Rural women able to use internet	8.5% (vs. 17.1% rural men) — NSSO 75th Round, 2017-18

Low digital literacy, language barriers, and a lack of skills in photography, pricing, and cataloguing keep skilled artisans out of India's digital economy.

💡 Our Solution

An AI-driven mobile app that acts as a virtual business manager — reducing the entire digital-onboarding journey to a few guided taps:

Register with basic business documents (multilingual app, in the artisan's own language)
Photograph the product — no professional skills needed
AI enhances the photo (background, lighting, framing)
AI writes a professional, SEO-friendly description from a voice note
AI suggests a real-time, competitive price by analyzing comparable listings
AI recommends the best-fit e-commerce/B2B marketplace
One tap pushes the listing live — auto-creating and auto-filling the seller's account if it's their first time on that platform

We're not building a new marketplace — we're building the missing AI layer that makes existing government and private digital-commerce infrastructure (Bhashini, ONDC, Indiahandmade, and more) actually usable by someone who's never sold online before.

✨ Key Features
Feature	Description
🎙️ Multilingual Voice Onboarding	Register and catalogue products by speaking in a regional language — powered by Bhashini
🖼️ AI Image Enhancer & Studio	Automatic background cleanup, lighting correction, and professional formatting
📝 Multilingual Auto-Cataloger	Voice-to-text → translated, SEO-friendly listings in English & Hindi
💰 Dynamic Pricing Assistant	AI-suggested price range from live comparable-listing analysis
🛒 Marketplace Recommender	Matches product category to the best-fit government/private channel
🔗 One-Tap Marketplace Push	Auto-fills seller onboarding forms on the destination platform
👤 Reusable Seller Profile	KYC and catalogue data stored once, reused across every platform
🛠️ Tech Stack
Frontend        Flutter (cross-platform: Android + iOS)
Backend/API      FastAPI (Python)
LLM Inference    Groq
Computer Vision  OpenCV
Multilingual AI  Bhashini (ASR · Translation · TTS)
Database/Auth    Supabase + PostgreSQL
Integrations     Marketplace APIs (ONDC / e-commerce) · Payment gateway APIs
🏗️ System Architecture
Flutter App
    │
    ▼
FastAPI Backend  ──────►  Supabase / PostgreSQL (seller profiles, catalogue, orders)
    │
    ├──► OpenCV            → Image enhancement pipeline
    ├──► Groq (LLM)         → Description generation, pricing logic
    ├──► Bhashini            → Speech-to-text, translation, text-to-speech
    │
    ▼
Marketplace & Payment APIs → Listing push, account creation, transactions

📌 Replace this block with your actual architecture diagram — docs/architecture.png recommended.

🔄 User Flow
Register (docs upload) → Upload photo → AI enhances photo → AI generates description
   → AI suggests price → AI recommends marketplace → One-tap push to platform
   → Auto-filled listing goes live → Artisan manages sales from the app
📸 Screenshots
Onboarding	AI Enhancement	Catalogue	Pricing
add screenshot	add screenshot	add screenshot	add screenshot
📊 Data & Research Sources
Ministry of Textiles / PIB — artisan population, sector data (Dec 2025)
4th All India Handloom Census, 2019-20
NSSO 75th Round (2017-18) — digital literacy & internet access
Digital Empowerment Foundation survey, n=2,000+ (via IDR, 2025)
PIB releases on ONDC (Mar 2026) and Indiahandmade (Jul 2026)
Bhashini — National Language Translation Mission (MeitY)
