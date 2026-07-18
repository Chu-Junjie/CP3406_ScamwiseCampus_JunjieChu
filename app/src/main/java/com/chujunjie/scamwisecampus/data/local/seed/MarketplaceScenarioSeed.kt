package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.MessageType
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

internal object MarketplaceScenarioSeed {

    val scenarios: List<Scenario> = listOf(
        Scenario(
            id = "marketplace_easy_01",
            title = "Payment Screenshot Buyer",
            category = ScamCategory.MARKETPLACE,
            difficulty = Difficulty.EASY,
            messageType = MessageType.MARKETPLACE_MESSAGE,
            sender = "Buyer",
            messageBody = """
                I have transferred the money. Here is the payment screenshot.
                Please send the laptop now because my courier is waiting.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                WarningSign(
                    id = "marketplace_easy_01_warning_01",
                    description = "The buyer relies on a screenshot as proof of payment.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "marketplace_easy_01_warning_02",
                    description = "The buyer creates pressure to release the item immediately.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "marketplace_easy_01_warning_03",
                    description = "The seller has not confirmed the payment in their own account.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "marketplace_easy_01_warning_04",
                    description = "The buyer suggests using the marketplace's protected payment system.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "marketplace_easy_01_action_01",
                    description = "Release the laptop because the screenshot looks genuine.",
                    isSafeAction = false,
                    feedback = "Screenshots can be edited and are not proof that funds were received."
                ),
                ActionOption(
                    id = "marketplace_easy_01_action_02",
                    description = "Confirm the payment in your own account before releasing the item.",
                    isSafeAction = true,
                    feedback = "Only the seller's own account can confirm that payment was received."
                ),
                ActionOption(
                    id = "marketplace_easy_01_action_03",
                    description = "Pay the buyer's courier fee first.",
                    isSafeAction = false,
                    feedback = "Unexpected courier fees are another common marketplace scam tactic."
                ),
                ActionOption(
                    id = "marketplace_easy_01_action_04",
                    description = "Send a copy of your identity document.",
                    isSafeAction = false,
                    feedback = "Identity documents are unnecessary for confirming payment."
                )
            ),
            explanation = """
                Fake payment screenshots and urgent courier stories are common
                marketplace fraud techniques.
            """.trimIndent(),
            verificationAdvice = """
                Confirm that cleared funds are visible in your own bank or protected
                marketplace account before releasing an item.
            """.trimIndent()
        ),
        Scenario(
            id = "marketplace_medium_01",
            title = "Deposit Before Viewing",
            category = ScamCategory.MARKETPLACE,
            difficulty = Difficulty.MEDIUM,
            messageType = MessageType.MARKETPLACE_MESSAGE,
            sender = "Seller",
            messageBody = """
                Many people are interested in the second-hand bicycle.
                Send a $50 deposit today and I will reserve it for you.
                You can inspect it tomorrow.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NEEDS_VERIFICATION,
            warningSigns = listOf(
                WarningSign(
                    id = "marketplace_medium_01_warning_01",
                    description = "The seller requests money before the item is inspected.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "marketplace_medium_01_warning_02",
                    description = "The seller uses competition to create urgency.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "marketplace_medium_01_warning_03",
                    description = "The item is second-hand.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "marketplace_medium_01_warning_04",
                    description = "The seller offers an inspection on another day.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "marketplace_medium_01_action_01",
                    description = "Pay the deposit to avoid losing the item.",
                    isSafeAction = false,
                    feedback = "Deposits sent before verification may be difficult to recover."
                ),
                ActionOption(
                    id = "marketplace_medium_01_action_02",
                    description = "Inspect the item and use the platform's protected payment method.",
                    isSafeAction = true,
                    feedback = "Inspection and protected payment reduce the risk."
                ),
                ActionOption(
                    id = "marketplace_medium_01_action_03",
                    description = "Send a larger deposit to prove commitment.",
                    isSafeAction = false,
                    feedback = "A larger payment increases the potential loss."
                ),
                ActionOption(
                    id = "marketplace_medium_01_action_04",
                    description = "Move the conversation to an unknown messaging application.",
                    isSafeAction = false,
                    feedback = "Leaving the platform may remove reporting and payment protections."
                )
            ),
            explanation = """
                A deposit request can sometimes be genuine, but payment before
                inspection and urgency require additional verification.
            """.trimIndent(),
            verificationAdvice = """
                Inspect the item in a safe public place and use the marketplace's
                protected payment process where available.
            """.trimIndent()
        ),
        Scenario(
            id = "marketplace_hard_01",
            title = "In-App Purchase Confirmation",
            category = ScamCategory.MARKETPLACE,
            difficulty = Difficulty.HARD,
            messageType = MessageType.MARKETPLACE_MESSAGE,
            sender = "Marketplace System",
            messageBody = """
                Your purchase has been confirmed through the platform's protected
                checkout. Payment will remain held until delivery is completed.
                View the order inside the marketplace application.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            warningSigns = listOf(
                WarningSign(
                    id = "marketplace_hard_01_warning_01",
                    description = "The message asks for payment outside the platform.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "marketplace_hard_01_warning_02",
                    description = "The message requests a password.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "marketplace_hard_01_warning_03",
                    description = "The order can be confirmed inside the existing application.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "marketplace_hard_01_warning_04",
                    description = "The message pressures the user to send money within minutes.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "marketplace_hard_01_action_01",
                    description = "Open the marketplace application and confirm the order.",
                    isSafeAction = true,
                    feedback = "The existing application provides an independent confirmation path."
                ),
                ActionOption(
                    id = "marketplace_hard_01_action_02",
                    description = "Pay the seller again through a direct bank transfer.",
                    isSafeAction = false,
                    feedback = "A second payment outside the platform removes protection."
                ),
                ActionOption(
                    id = "marketplace_hard_01_action_03",
                    description = "Share the account password with the seller.",
                    isSafeAction = false,
                    feedback = "Account passwords must never be shared."
                ),
                ActionOption(
                    id = "marketplace_hard_01_action_04",
                    description = "Cancel every marketplace transaction automatically.",
                    isSafeAction = false,
                    feedback = "Legitimate protected transactions can be verified inside the application."
                )
            ),
            explanation = """
                The confirmation uses the platform's protected checkout and directs
                the user to an existing application rather than an external link.
            """.trimIndent(),
            verificationAdvice = """
                Confirm the order, payment status, and seller details inside the
                official marketplace application.
            """.trimIndent()
        )
    )
}
