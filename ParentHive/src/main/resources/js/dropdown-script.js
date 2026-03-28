function initProfileDropdown() {
    let profileArrow = document.querySelector(".profile-arrow");
    
    console.log("Attempting to initialize dropdown...");
    console.log("profileArrow element:", profileArrow);

    if (profileArrow) {
        let profileWrapper = profileArrow.parentElement;
        console.log("profileWrapper element:", profileWrapper);
        console.log("profileWrapper classes:", profileWrapper.className);

        let hideTimeoutId = null;

        // Toggle dropdown on profile click
        profileArrow.onclick = function(e) {
            console.log("Dropdown clicked!");
            e.stopPropagation();
            profileWrapper.classList.toggle("open");
            console.log("After toggle, classes:", profileWrapper.className);
        };

        // Handle hover to show dropdown for 3 FULL SECONDS
        profileWrapper.addEventListener("mouseenter", function() {
            console.log("✅ Mouse entered - dropdown will stay visible for 3 FULL SECONDS");
            // Clear any pending hide timeout
            if (hideTimeoutId) {
                clearTimeout(hideTimeoutId);
                hideTimeoutId = null;
            }
            // Add persistent open class - keeps dropdown visible
            profileWrapper.classList.add("persistent-open");
        });

        profileWrapper.addEventListener("mouseleave", function() {
            console.log("⏱️  Mouse left - 3 second countdown started...");
            // Set timeout to hide dropdown after 3 FULL SECONDS
            hideTimeoutId = setTimeout(function() {
                console.log("⏰ 3 FULL SECONDS passed - hiding dropdown NOW");
                profileWrapper.classList.remove("persistent-open");
                hideTimeoutId = null;
            }, 3000); // 3000 milliseconds = 3 full seconds
        });

        console.log("Profile dropdown initialized successfully");
        return true;
    }
    console.log("Profile dropdown NOT initialized - profileArrow not found");
    return false;
}

// Try to initialize immediately
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initProfileDropdown);
} else {
    initProfileDropdown();
}

// Keep trying every 100ms until it works
let initAttempts = 0;
let initInterval = setInterval(function() {
    if (initProfileDropdown()) {
        clearInterval(initInterval);
    }
    initAttempts++;
    if (initAttempts > 50) {
        clearInterval(initInterval);
        console.log("Failed to initialize dropdown after 5 seconds");
    }
}, 100);

// Confirm delete functionality
function confirmDelete(event, formId) {
    event.preventDefault();
    event.stopPropagation();
    const confirmed = confirm("Are you sure you want to delete this post?");
    if (confirmed) {
        document.getElementById(formId).submit();
    }
}

