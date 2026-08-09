package com.example.multitiendaapp.presentation.selectRole

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multitiendaapp.R

//Pantalla de selección de rol:
@Composable
fun SelectRoleScreen(
    onGoToRegisterSeller: () -> Unit,
    onGoToRegisterCustomer: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
//        Creamos una tarjeta o Card de registro de vendedor:
        //   Que sea clickable para llegar va la pantalla de registro del vendedor
        //   o RegisterSellerScreen():
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onGoToRegisterSeller() } //Para llegar a RegisterSellerScreen()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = R.drawable.ic_seller),
                    contentDescription = "Registro de vendedor",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Registrarme como vendedor")
            }

        }

//        Creamos una tarjeta o Card de registro de cliente:
//        //   Que sea clickable para llegar va la pantalla de registro del cliente
//        //   o RegisterCustomerScreen():
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onGoToRegisterCustomer() } //Para llegar a RegisterCustomerScreen()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = R.drawable.ic_customer),
                    contentDescription = "Registro de cliente",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Registrarme como cliente")
            }
        }

    }

}