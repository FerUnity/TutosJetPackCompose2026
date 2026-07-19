package com.example.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.example.data.database.ContactDao
import com.example.data.database.LocalContactEntity
import com.example.data.database.CallRecordEntity
import com.example.data.database.FavoriteOverrideEntity
import com.example.data.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID

class ContactRepository(
    private val context: Context,
    private val contactDao: ContactDao
) {
    // Flow of local database contacts
    val localContactsFlow: Flow<List<Contact>> = contactDao.getAllLocalContacts().map { entities ->
        entities.map { it.toModel() }
    }

    // Flow of favorite overrides for system contacts
    private val favoriteOverridesFlow: Flow<Map<String, Boolean>> = contactDao.getAllFavoriteOverrides().map { list ->
        list.associate { it.contactId to it.isFavorite }
    }

    // Combined flow that merges local contacts and system contacts (if permission granted)
    fun getMergedContactsFlow(hasPermission: Boolean): Flow<List<Contact>> {
        val systemContacts = if (hasPermission) {
            getSystemContacts()
        } else {
            emptyList()
        }

        return combine(localContactsFlow, favoriteOverridesFlow) { local, overrides ->
            val all = mutableListOf<Contact>()
            // Add all local contacts
            all.addAll(local)

            // Add system contacts, filtering out any that might overlap by phone number, or just appending
            systemContacts.forEach { sysContact ->
                // Avoid duplicating if phone number already exists in local
                val exists = all.any { it.phone.replace(" ", "") == sysContact.phone.replace(" ", "") }
                if (!exists) {
                    val overrideFav = overrides[sysContact.id] ?: sysContact.isFavorite
                    all.add(sysContact.copy(isFavorite = overrideFav))
                }
            }

            // Sort alphabetically by name
            all.sortedBy { it.name.lowercase() }
        }
    }

    // Call history flow
    val callRecordsFlow: Flow<List<CallRecordEntity>> = contactDao.getAllCallRecords()

    // Query system contacts
    private fun getSystemContacts(): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return contactsList
        }

        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (it.moveToNext()) {
                val sysId = if (idIndex >= 0) it.getString(idIndex) else ""
                val name = if (nameIndex >= 0) it.getString(nameIndex) else ""
                val phone = if (numberIndex >= 0) it.getString(numberIndex) else ""
                val photo = if (photoIndex >= 0) it.getString(photoIndex) ?: "" else ""

                if (sysId.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()) {
                    val id = "system_$sysId"
                    contactsList.add(
                        Contact(
                            id = id,
                            name = name,
                            phone = phone,
                            address = "", // StructuredPostal or fallback empty
                            company = "Contacto del Sistema",
                            photoUri = photo,
                            isFavorite = false,
                            isSystemContact = true
                        )
                    )
                }
            }
        }

        // De-duplicate system contacts by phone number
        return contactsList.distinctBy { it.phone.replace(" ", "") }
    }

    // Initialize default contacts if DB is empty
    suspend fun initializeDefaultContactsIfEmpty() {
        contactDao.getAllLocalContacts().map { it.isEmpty() }.collect { isEmpty ->
            if (isEmpty) {
                getDefaultSaharaContacts().forEach {
                    contactDao.insertLocalContact(it)
                }
            }
        }
    }

    // Add a local contact
    suspend fun addLocalContact(name: String, phone: String, address: String, company: String, photoUri: String, isFavorite: Boolean) {
        val id = "local_${UUID.randomUUID()}"
        val entity = LocalContactEntity(
            id = id,
            name = name,
            phone = phone,
            address = address,
            company = company,
            photoUri = photoUri,
            isFavorite = isFavorite,
            isSystemContact = false
        )
        contactDao.insertLocalContact(entity)
    }

    // Toggle favorite status
    suspend fun toggleFavorite(contact: Contact) {
        if (contact.isSystemContact) {
            // Save favorite override in Room for system contact
            contactDao.insertFavoriteOverride(
                FavoriteOverrideEntity(
                    contactId = contact.id,
                    isFavorite = !contact.isFavorite
                )
            )
        } else {
            // Directly update the local contact entity
            val entity = LocalContactEntity(
                id = contact.id,
                name = contact.name,
                phone = contact.phone,
                address = contact.address,
                company = contact.company,
                photoUri = contact.photoUri,
                isFavorite = !contact.isFavorite,
                isSystemContact = false
            )
            contactDao.insertLocalContact(entity)
        }
    }

    // Delete contact
    suspend fun deleteContact(contact: Contact) {
        if (!contact.isSystemContact) {
            val entity = LocalContactEntity(
                id = contact.id,
                name = contact.name,
                phone = contact.phone,
                address = contact.address,
                company = contact.company,
                photoUri = contact.photoUri,
                isFavorite = contact.isFavorite,
                isSystemContact = false
            )
            contactDao.deleteLocalContact(entity)
        }
    }

    // Add call record
    suspend fun addCallRecord(record: CallRecordEntity) {
        contactDao.insertCallRecord(record)
    }

    // Clear all call records
    suspend fun clearCallRecords() {
        contactDao.clearAllCallRecords()
    }

    // Default Sahara contacts helper
    private fun getDefaultSaharaContacts(): List<LocalContactEntity> {
        return listOf(
            LocalContactEntity(
                id = "default_1",
                name = "Elena Varas",
                phone = "+34 612 345 678",
                address = "Calle de la Luna 45, Madrid",
                company = "Sahara Curators",
                photoUri = "https://lh3.googleusercontent.com/aida-public/AB6AXuDVGv_N9eiUKGsa81flwHiMLGUNN_ApEJpHM6B_TTg_NVRIWyze_JYTqhXL-W-WJmPgpTyL67wWsZbfnKAMmnJtgOCBhng99tbKyEZ7wWGUFC3JaeC_ZVMyHmne56G9zVDXOQIQJvh5VwD97mVrpdMsPO_fkKa-QbQD8bMH15pm_IGVH1erySwSCyqD4kuk22BWJBJ9azSj32SNnpdsvnxwigA6qtOTrClROSREb88tjSeLPmfY2Q_IHPLQyIiZpAihJOo_EHzciKc",
                isFavorite = true,
                isSystemContact = false
            ),
            LocalContactEntity(
                id = "default_2",
                name = "Mateo Rivera",
                phone = "+34 699 000 111",
                address = "Paseo del Sol 12, Marbella",
                company = "Rivera Design Studio",
                photoUri = "https://lh3.googleusercontent.com/aida-public/AB6AXuAUfL__MozBh1gS51qyeca2KRzBLOzWGkKlYjGgEZprxV6InWM48Mq6KQ2K-6Op69aUXMziXfUSFUGuBbGljr-29sy_mcmeSllLXvBcZnXcxu48yNvMEiqQQe7wmFPMbC6LhNDWOB6Mz5jJlbsFtzZ0ZeNPIrxkPHTwnx-t7ckfVrY3Pz_po-EjtvXlvhkgkR4GIogguRvuX7lUFB1mqf8Ja6Jpw6flc-jkpnV2GX5pyUS8XX8WbjQmlEd11Pv76JCaWMJftTYKUHQ",
                isFavorite = false,
                isSystemContact = false
            ),
            LocalContactEntity(
                id = "default_3",
                name = "Julian Sahara",
                phone = "+34 600 555 222",
                address = "Av. de los Olivos, Sevilla",
                company = "Sahara Corp",
                photoUri = "https://lh3.googleusercontent.com/aida-public/AB6AXuBd1YGjWAIqTPDy9aqv8lwdY1egtKPGZngt5Pcl4t3sQQvApR3rcKW9ZPAhX50gNKZaTU-cSl3s4ZeIUtTC7fZiNKvO9P6WTKWsT4FVWW6ZEt_l88QsjjpVEFqgchbpvhmB9ErzQhgrPnzjI-pPXfgLijBBaT4mc0Zj0oJ2LDd9xiJCrmC0R0k_foB2N1Wb1PmaC5t7CT2VkL2p3aue3sbuFnSv91Z0meGZzasHp91J89gsWxftujF1kCx84Co4ALXV76e4dFhjZ68",
                isFavorite = false,
                isSystemContact = false
            ),
            LocalContactEntity(
                id = "default_4",
                name = "Sofía Castillo",
                phone = "+34 688 777 666",
                address = "Gran Vía 89, Barcelona",
                company = "Castillo Arts",
                photoUri = "https://lh3.googleusercontent.com/aida-public/AB6AXuBq6TDLKvlKfY5z3L1yRf9AE7ZDa8WjqB2v4SDu-scE5sh9temsPmJxbqtjsvoKdDrBxqtMHm-aDUKhClZycAO_lx4CDHc0AoFtGSNMRHFlzHzlaovuSYYd7_IkfRusa-ATRlIK6TEdGbmy_VThY-UAtHOPHGjuqdsiRcnAwWMzVcdDuINti8h2TX3JXKbAPZIzw8NX3yjMKhbDA22M0JFlLVfx4K09YhksvDMeVfdQdBMdToUj0UHIZkPRB6l2F3_VT2zvN8Ifpng",
                isFavorite = true,
                isSystemContact = false
            )
        )
    }

    // Helper conversion
    private fun LocalContactEntity.toModel() = Contact(
        id = id,
        name = name,
        phone = phone,
        address = address,
        company = company,
        photoUri = photoUri,
        isFavorite = isFavorite,
        isSystemContact = isSystemContact
    )
}
