# Uncomment the next line to define a global platform for your project
platform :ios, '11.0'

def shared_pods
    # Pods for loopr-ios
    pod 'Charts', '3.2.1'
    pod 'SwiftLint'
    pod 'SwiftTheme'
    pod 'Socket.IO-Client-Swift', '~> 13.1.0'
    pod 'NotificationBannerSwift', '~> 1.6.3'
    pod 'SVProgressHUD'
    pod 'ESTabBarController-swift'
    pod 'SwiftyMarkdown'
    pod 'CDMarkdownKit', '1.2.1'
    pod 'MKDropdownMenu'
    pod 'StepSlider', git: 'https://github.com/xiaowheat/StepSlider.git'
    
    # TODO: deprecated in in 1.7
    pod 'Fabric'
    pod 'Crashlytics'
    
    pod 'UMCCommon'
    pod 'UMCAnalytics'

    pod 'SDWebImage', git: 'https://github.com/SDWebImage/SDWebImage.git', :branch => '5.x'
    
    # Note from cocoapods https://github.com/CocoaPods/CocoaPods/issues/7238
    # https://developer.umeng.com/docs/66632/detail/67204?spm=a311a.9588098.0.0
    # [!] 'UMCSecurityPlugins' uses the unencrypted http protocol to transfer the Pod. Please be sure you're in a safe network with only trusted hosts in there. Please reach out to the library author to notify them of this security issue.
    # ruby: I think we don't need to use this pod.
    # pod 'UMCSecurityPlugins'
    
    pod 'lottie-ios'
    pod 'WeChat_SDK', '~> 1.8.1'
    
    # Pods for keystone
    pod 'Geth', '1.8.8'
    pod 'BigInt', '3.0.1'
    pod 'CryptoSwift', '0.8.3'
    pod 'secp256k1_ios', git: 'https://github.com/xiaowheat/secp256k1_ios.git'
    pod 'TrezorCrypto', '0.0.9', inhibit_warnings: true
    pod 'SipHash', '1.2.0'
    
    pod 'web3.swift', git: 'https://github.com/Loopring/web3.swift.git'
end

target 'loopr-ios' do
  # Comment the next line if you're not using Swift and don't want to use dynamic frameworks
  use_frameworks!

  shared_pods


  target 'loopr-iosTests' do
    inherit! :search_paths
    # Pods for testing
  end

  target 'loopr-iosUITests' do
    inherit! :search_paths
    # Pods for testing
  end

end
